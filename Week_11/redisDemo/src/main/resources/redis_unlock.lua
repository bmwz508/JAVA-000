---
--- redis分布式锁解锁
--- DateTime: 2019/11/27 14:45
---
local getValue = redis.call('get', KEYS[1])
if getValue == false then
    return 1
end
if getValue ~= ARGV[1] then
    return 0
end
return redis.call('del', KEYS[1])