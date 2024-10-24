import config
from sshtunnel import SSHTunnelForwarder


with SSHTunnelForwarder(
    (config.SSH_TUNNEL_MACHINE['ip'], config.SSH_TUNNEL_MACHINE['port']),
    ssh_username=config.SSH_TUNNEL_MACHINE['username'],
    ssh_password=config.SSH_TUNNEL_MACHINE['password'],
    remote_bind_address=(config.SONAR_SERVER['ip'], config.SONAR_SERVER['port']),
    local_bind_address=(config.LOCAL_SERVER['ip'], config.SONAR_SERVER['port'])
) as tunnel:
    input('关闭')


