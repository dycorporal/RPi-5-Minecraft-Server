from smbus2 import SMBus
import time
import warnings
import subprocess
import datetime
from mcrcon import MCRcon

# Define some device constants
LCD_ADDRESS = 0x27  # Change this if your display shows different address in i2cdetect
LCD_CHR = 1  # Mode - Sending data
LCD_CMD = 0  # Mode - Sending command

LCD_LINE_1 = 0x80  # LCD RAM address for the 1st line
LCD_LINE_2 = 0xC0  # LCD RAM address for the 2nd line
LCD_LINE_3 = 0x94  # LCD RAM address for the 3rd line
LCD_LINE_4 = 0xD4  # LCD RAM address for the 4th line

LCD_BACKLIGHT = 0x08  # On
ENABLE = 0b00000100  # Enable bit

bus = SMBus(1)  # Rev 2 Pi uses 1

RCON_HOST = "localhost"  # or your server IP if remote
RCON_PASSWORD = "password"  # set this to your rcon.password from server.properties
RCON_PORT = 25575  # default port, change if you modified it


def lcd_init():
    # Initialize display
    lcd_byte(0x33, LCD_CMD)  # 110011 Initialize
    lcd_byte(0x32, LCD_CMD)  # 110010 Initialize
    lcd_byte(0x06, LCD_CMD)  # 000110 Cursor move direction
    lcd_byte(0x0C, LCD_CMD)  # 001100 Display On, Cursor Off, Blink Off
    lcd_byte(0x28, LCD_CMD)  # 101000 Data length, number of lines, font size
    lcd_byte(0x01, LCD_CMD)  # 000001 Clear display
    time.sleep(0.0005)       # Wait for clear display command to process

def lcd_byte(bits, mode):
    # Send byte to data pins
    # bits = data
    # mode = 1 for data, 0 for command
    bits_high = mode | (bits & 0xF0) | LCD_BACKLIGHT
    bits_low = mode | ((bits << 4) & 0xF0) | LCD_BACKLIGHT

    # High bits
    bus.write_byte(LCD_ADDRESS, bits_high)
    lcd_toggle_enable(bits_high)

    # Low bits
    bus.write_byte(LCD_ADDRESS, bits_low)
    lcd_toggle_enable(bits_low)

def lcd_toggle_enable(bits):
    # Toggle enable
    time.sleep(0.0005)
    bus.write_byte(LCD_ADDRESS, (bits | ENABLE))
    time.sleep(0.0005)
    bus.write_byte(LCD_ADDRESS, (bits & ~ENABLE))
    time.sleep(0.0005)

def lcd_string(message, line):
    # Send string to display
    message = message.ljust(20," ")  # Pad with spaces
    lcd_byte(line, LCD_CMD)
    for i in range(len(message)):
        lcd_byte(ord(message[i]), LCD_CHR)
def main():
    # Main program block
    lcd_init()
    i = 1
    z = 0
    try:
        with MCRcon(RCON_HOST, RCON_PASSWORD, RCON_PORT) as mcr:
            # Send list command to get players
            print("Server connected!")
            while True:
                global lines
                global current_display
                if (i % 5) == 0:
                    output = subprocess.check_output(['/usr/bin/vcgencmd','measure_temp']).decode()
                    temperature = float(output.split("=")[1].split("'")[0])
                    lcd_string(f"CPU: {temperature} C", LCD_LINE_1)
                    memoryoutput = subprocess.check_output(['free','-m']).decode().split(" ")
                    memorylist = list(filter(lambda x: x != '', memoryoutput))
                    usedMemory = round(float(memorylist[7])/1024,2)
                    totalMemory = round(float(memorylist[6])/1024,2)
                    lcd_string(f'RAM:{usedMemory}GB/{totalMemory}GB ', LCD_LINE_2)
                    
                    resp = mcr.command("list")
                    #print(resp)
                    lcd_string(f"Players: {resp.split(' ')[2]}/{resp.split(' ')[7]}", LCD_LINE_3)
                    
                    i = 1
                else:
                    i +=1
                    z +=1
                #lcd_string(f"{i}",LCD_LINE_3)
                time.sleep(0.25)
                current = datetime.datetime.now()
                textstring = f"{current.day}/{current.month}/{current.year} {current.hour:02d}:{current.minute:02d}"
                lcd_string(f'{textstring}', LCD_LINE_4)
                # Parse the response
                # Response format: "There are X of max Y players online: [player names]"
                #players = resp.split("There are ")[1].split(" of")[0]  
    except Exception as e:
        print(f"Error connecting to server: {e}")
        return -1
    
        
if __name__ == '__main__':
    try:
        lines = [LCD_LINE_1, LCD_LINE_2, LCD_LINE_3, LCD_LINE_4]
        current_display = ["", "", "", ""]
        time.sleep(3)
        main()
    except KeyboardInterrupt:
        pass
    finally:
        lcd_byte(0x01, LCD_CMD)  # Clear display

