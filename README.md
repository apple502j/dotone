# DotOne
Brings the .1 experience to 1.19.0. Without allay duping and report stuff, that is.

ARR for now.

## Things included
- 1.19.0 server compatibility (can be installed on either side)
- Almost all bugfixes (incl. private bugs)
- Bedrock roof hole fix
- Cured villager trade refreshing fix
- Stronghold locating crash fix
- run_command no longer sends chat messages, and commands sent that way are unsigned
- Out-of-order command fix

## Things not included
- **INCOMPATIBLE WITH REAL 1.19.1**
- Chat reporting
- Ban screen (you'll still get the "multiplayer disabled" error present in modern versions, and you can't play on online mode servers when banned)
- Allay duplication and allay bug fixes
- "Player public key theft" exploit fix (does not affect anyone unless enforce-secure-profile is set to true)
- Action bar narrating/system info narrating is partially implemented. Server-sent system infos (e.g. beds) are not narrated, but client-only system infos (e.g. riding entity, using saved hotbar, etc) are.
- Netty library bump; you must replace the binary manually.
