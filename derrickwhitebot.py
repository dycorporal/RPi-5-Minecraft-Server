import discord
from discord.ext import commands
from mcrcon import MCRcon
import logging

# Set up logging to see what's happening
logging.basicConfig(level=logging.DEBUG)

# Set up intents
intents = discord.Intents.default()
intents.message_content = True
intents.members = True

# Create bot with command prefix '!'
bot = commands.Bot(command_prefix='!', intents=intents)

@bot.event
async def on_ready():
    print(f'Logged in as {bot.user.name}')
    print(f'Bot ID: {bot.user.id}')
    print('------')
    print('Connected to the following servers:')
    for guild in bot.guilds:
        print(f'- {guild.name} (id: {guild.id})')
@bot.event
async def on_message(message):
    if message.author == bot.user:
        return
    if message.content.startswith('!mc'):
        try:
            RCON_HOST = "localhost"  # or your server IP if remote
            RCON_PASSWORD = "password"  # set this to your rcon.password from server.properties
            RCON_PORT = 25575  # default port, change if you modified it
            with MCRcon(RCON_HOST, RCON_PASSWORD, RCON_PORT) as mcr:
                response = mcr.command("list")
                await message.channel.send(f"Server is up. - {response}")
                
        except Exception as e:
            await message.channel.send(f'Server is in error state or not up {e}')
    if message.content.startswith('!help'):
        await message.channel.send('!help - You just used this. Prints out all commands and usages. Usage: !help')
        await message.channel.send('!role - Toggles a role color out of purple, green, blue, red. Usage: !role red')
        await message.channel.send('!mc - Says whether server is on, and players active. Usage: !mc')
    if message.content.startswith('!role'):
        # Find role name
        role_name = str(message.content).split(' ')[1]
        print(role_name)
        # search corresponding Discord role
        role = discord.utils.get(message.guild.roles, name=role_name)
        # Check if the role exists
        colors = ['red','green','purple','blue']
        if role is None:
            await message.channel.send(f'Role "{role_name}" cannot be added')
            return
        # Role assignment
        if role_name in colors:
            if role in message.author.roles:
                await message.author.remove_roles(role)
                await message.channel.send(f'Role "{role_name}" was removed from {message.author}')
            else:
                await message.author.add_roles(role)
                await message.channel.send(f'Role "{role_name}" was added to {message.author}')
        else:
            await message.channel.send('Error found while trying to add/remove a role') #Changed 
        
# Your other commands here...

try:
    print("Starting bot...")
    bot.run('TOKEN')  # Make sure this is your correct token. You can get this from the discord developer portal
except discord.LoginFailure:
    print("Failed to login: Invalid token")
except Exception as e:
    print(f"An error occurred: {str(e)}")
