begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.redis
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|redis
package|;
end_package

begin_enum
DECL|enum|Command
specifier|public
enum|enum
name|Command
block|{
DECL|enumConstant|PING
DECL|enumConstant|SET
DECL|enumConstant|GET
DECL|enumConstant|QUIT
DECL|enumConstant|EXISTS
DECL|enumConstant|DEL
DECL|enumConstant|TYPE
DECL|enumConstant|FLUSHDB
DECL|enumConstant|KEYS
DECL|enumConstant|RANDOMKEY
name|PING
block|,
name|SET
block|,
name|GET
block|,
name|QUIT
block|,
name|EXISTS
block|,
name|DEL
block|,
name|TYPE
block|,
name|FLUSHDB
block|,
name|KEYS
block|,
name|RANDOMKEY
block|,
DECL|enumConstant|RENAME
DECL|enumConstant|RENAMENX
DECL|enumConstant|RENAMEX
DECL|enumConstant|DBSIZE
DECL|enumConstant|EXPIRE
DECL|enumConstant|EXPIREAT
DECL|enumConstant|TTL
DECL|enumConstant|SELECT
name|RENAME
block|,
name|RENAMENX
block|,
name|RENAMEX
block|,
name|DBSIZE
block|,
name|EXPIRE
block|,
name|EXPIREAT
block|,
name|TTL
block|,
name|SELECT
block|,
DECL|enumConstant|MOVE
DECL|enumConstant|FLUSHALL
DECL|enumConstant|GETSET
DECL|enumConstant|MGET
DECL|enumConstant|SETNX
DECL|enumConstant|SETEX
DECL|enumConstant|MSET
DECL|enumConstant|MSETNX
DECL|enumConstant|DECRBY
name|MOVE
block|,
name|FLUSHALL
block|,
name|GETSET
block|,
name|MGET
block|,
name|SETNX
block|,
name|SETEX
block|,
name|MSET
block|,
name|MSETNX
block|,
name|DECRBY
block|,
DECL|enumConstant|DECR
DECL|enumConstant|INCRBY
DECL|enumConstant|INCR
DECL|enumConstant|APPEND
DECL|enumConstant|SUBSTR
DECL|enumConstant|HSET
DECL|enumConstant|HGET
DECL|enumConstant|HSETNX
DECL|enumConstant|HMSET
DECL|enumConstant|HMGET
name|DECR
block|,
name|INCRBY
block|,
name|INCR
block|,
name|APPEND
block|,
name|SUBSTR
block|,
name|HSET
block|,
name|HGET
block|,
name|HSETNX
block|,
name|HMSET
block|,
name|HMGET
block|,
DECL|enumConstant|HINCRBY
DECL|enumConstant|HEXISTS
DECL|enumConstant|HDEL
DECL|enumConstant|HLEN
DECL|enumConstant|HKEYS
DECL|enumConstant|HVALS
DECL|enumConstant|HGETALL
DECL|enumConstant|RPUSH
DECL|enumConstant|LPUSH
name|HINCRBY
block|,
name|HEXISTS
block|,
name|HDEL
block|,
name|HLEN
block|,
name|HKEYS
block|,
name|HVALS
block|,
name|HGETALL
block|,
name|RPUSH
block|,
name|LPUSH
block|,
DECL|enumConstant|LLEN
DECL|enumConstant|LRANGE
DECL|enumConstant|LTRIM
DECL|enumConstant|LINDEX
DECL|enumConstant|LSET
DECL|enumConstant|LREM
DECL|enumConstant|LPOP
DECL|enumConstant|RPOP
DECL|enumConstant|RPOPLPUSH
DECL|enumConstant|SADD
name|LLEN
block|,
name|LRANGE
block|,
name|LTRIM
block|,
name|LINDEX
block|,
name|LSET
block|,
name|LREM
block|,
name|LPOP
block|,
name|RPOP
block|,
name|RPOPLPUSH
block|,
name|SADD
block|,
DECL|enumConstant|SMEMBERS
DECL|enumConstant|SREM
DECL|enumConstant|SPOP
DECL|enumConstant|SMOVE
DECL|enumConstant|SCARD
DECL|enumConstant|SISMEMBER
DECL|enumConstant|SINTER
DECL|enumConstant|SINTERSTORE
name|SMEMBERS
block|,
name|SREM
block|,
name|SPOP
block|,
name|SMOVE
block|,
name|SCARD
block|,
name|SISMEMBER
block|,
name|SINTER
block|,
name|SINTERSTORE
block|,
DECL|enumConstant|SUNION
DECL|enumConstant|SUNIONSTORE
DECL|enumConstant|SDIFF
DECL|enumConstant|SDIFFSTORE
DECL|enumConstant|SRANDMEMBER
DECL|enumConstant|ZADD
DECL|enumConstant|ZRANGE
name|SUNION
block|,
name|SUNIONSTORE
block|,
name|SDIFF
block|,
name|SDIFFSTORE
block|,
name|SRANDMEMBER
block|,
name|ZADD
block|,
name|ZRANGE
block|,
DECL|enumConstant|ZREM
DECL|enumConstant|ZINCRBY
DECL|enumConstant|ZRANK
DECL|enumConstant|ZREVRANK
DECL|enumConstant|ZREVRANGE
DECL|enumConstant|ZCARD
DECL|enumConstant|ZSCORE
DECL|enumConstant|MULTI
DECL|enumConstant|DISCARD
name|ZREM
block|,
name|ZINCRBY
block|,
name|ZRANK
block|,
name|ZREVRANK
block|,
name|ZREVRANGE
block|,
name|ZCARD
block|,
name|ZSCORE
block|,
name|MULTI
block|,
name|DISCARD
block|,
DECL|enumConstant|EXEC
DECL|enumConstant|WATCH
DECL|enumConstant|UNWATCH
DECL|enumConstant|SORT
DECL|enumConstant|BLPOP
DECL|enumConstant|BRPOP
DECL|enumConstant|AUTH
DECL|enumConstant|SUBSCRIBE
DECL|enumConstant|PUBLISH
name|EXEC
block|,
name|WATCH
block|,
name|UNWATCH
block|,
name|SORT
block|,
name|BLPOP
block|,
name|BRPOP
block|,
name|AUTH
block|,
name|SUBSCRIBE
block|,
name|PUBLISH
block|,
DECL|enumConstant|UNSUBSCRIBE
DECL|enumConstant|PSUBSCRIBE
DECL|enumConstant|PUNSUBSCRIBE
DECL|enumConstant|ZCOUNT
DECL|enumConstant|ZRANGEBYSCORE
DECL|enumConstant|ZREVRANGEBYSCORE
name|UNSUBSCRIBE
block|,
name|PSUBSCRIBE
block|,
name|PUNSUBSCRIBE
block|,
name|ZCOUNT
block|,
name|ZRANGEBYSCORE
block|,
name|ZREVRANGEBYSCORE
block|,
DECL|enumConstant|ZREMRANGEBYRANK
DECL|enumConstant|ZREMRANGEBYSCORE
DECL|enumConstant|ZUNIONSTORE
DECL|enumConstant|ZINTERSTORE
DECL|enumConstant|SAVE
DECL|enumConstant|BGSAVE
name|ZREMRANGEBYRANK
block|,
name|ZREMRANGEBYSCORE
block|,
name|ZUNIONSTORE
block|,
name|ZINTERSTORE
block|,
name|SAVE
block|,
name|BGSAVE
block|,
DECL|enumConstant|BGREWRITEAOF
DECL|enumConstant|LASTSAVE
DECL|enumConstant|SHUTDOWN
DECL|enumConstant|INFO
DECL|enumConstant|MONITOR
DECL|enumConstant|SLAVEOF
DECL|enumConstant|CONFIG
DECL|enumConstant|STRLEN
name|BGREWRITEAOF
block|,
name|LASTSAVE
block|,
name|SHUTDOWN
block|,
name|INFO
block|,
name|MONITOR
block|,
name|SLAVEOF
block|,
name|CONFIG
block|,
name|STRLEN
block|,
DECL|enumConstant|SYNC
DECL|enumConstant|LPUSHX
DECL|enumConstant|PERSIST
DECL|enumConstant|RPUSHX
DECL|enumConstant|ECHO
DECL|enumConstant|LINSERT
DECL|enumConstant|DEBUG
DECL|enumConstant|BRPOPLPUSH
DECL|enumConstant|SETBIT
name|SYNC
block|,
name|LPUSHX
block|,
name|PERSIST
block|,
name|RPUSHX
block|,
name|ECHO
block|,
name|LINSERT
block|,
name|DEBUG
block|,
name|BRPOPLPUSH
block|,
name|SETBIT
block|,
DECL|enumConstant|GETBIT
DECL|enumConstant|SETRANGE
DECL|enumConstant|GETRANGE
DECL|enumConstant|PEXPIRE
DECL|enumConstant|PEXPIREAT
name|GETBIT
block|,
name|SETRANGE
block|,
name|GETRANGE
block|,
name|PEXPIRE
block|,
name|PEXPIREAT
block|,
DECL|enumConstant|GEOADD
DECL|enumConstant|GEODIST
DECL|enumConstant|GEOHASH
DECL|enumConstant|GEOPOS
DECL|enumConstant|GEORADIUS
DECL|enumConstant|GEORADIUSBYMEMBER
name|GEOADD
block|,
name|GEODIST
block|,
name|GEOHASH
block|,
name|GEOPOS
block|,
name|GEORADIUS
block|,
name|GEORADIUSBYMEMBER
block|}
end_enum

end_unit

