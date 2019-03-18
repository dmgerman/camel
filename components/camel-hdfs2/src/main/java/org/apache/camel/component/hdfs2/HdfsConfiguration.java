begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hdfs2
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hdfs2
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|Metadata
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|UriParam
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|UriParams
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|UriPath
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|URISupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|io
operator|.
name|SequenceFile
import|;
end_import

begin_class
annotation|@
name|UriParams
DECL|class|HdfsConfiguration
specifier|public
class|class
name|HdfsConfiguration
block|{
DECL|field|uri
specifier|private
name|URI
name|uri
decl_stmt|;
DECL|field|wantAppend
specifier|private
name|boolean
name|wantAppend
decl_stmt|;
DECL|field|splitStrategies
specifier|private
name|List
argument_list|<
name|HdfsProducer
operator|.
name|SplitStrategy
argument_list|>
name|splitStrategies
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|hostName
specifier|private
name|String
name|hostName
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|defaultValue
operator|=
literal|""
operator|+
name|HdfsConstants
operator|.
name|DEFAULT_PORT
argument_list|)
DECL|field|port
specifier|private
name|int
name|port
init|=
name|HdfsConstants
operator|.
name|DEFAULT_PORT
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|path
specifier|private
name|String
name|path
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|overwrite
specifier|private
name|boolean
name|overwrite
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|append
specifier|private
name|boolean
name|append
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|splitStrategy
specifier|private
name|String
name|splitStrategy
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|defaultValue
operator|=
literal|""
operator|+
name|HdfsConstants
operator|.
name|DEFAULT_BUFFERSIZE
argument_list|)
DECL|field|bufferSize
specifier|private
name|int
name|bufferSize
init|=
name|HdfsConstants
operator|.
name|DEFAULT_BUFFERSIZE
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|defaultValue
operator|=
literal|""
operator|+
name|HdfsConstants
operator|.
name|DEFAULT_REPLICATION
argument_list|)
DECL|field|replication
specifier|private
name|short
name|replication
init|=
name|HdfsConstants
operator|.
name|DEFAULT_REPLICATION
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|defaultValue
operator|=
literal|""
operator|+
name|HdfsConstants
operator|.
name|DEFAULT_BLOCKSIZE
argument_list|)
DECL|field|blockSize
specifier|private
name|long
name|blockSize
init|=
name|HdfsConstants
operator|.
name|DEFAULT_BLOCKSIZE
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|defaultValue
operator|=
literal|"NONE"
argument_list|)
DECL|field|compressionType
specifier|private
name|SequenceFile
operator|.
name|CompressionType
name|compressionType
init|=
name|HdfsConstants
operator|.
name|DEFAULT_COMPRESSIONTYPE
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|defaultValue
operator|=
literal|"DEFAULT"
argument_list|)
DECL|field|compressionCodec
specifier|private
name|HdfsCompressionCodec
name|compressionCodec
init|=
name|HdfsConstants
operator|.
name|DEFAULT_CODEC
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"NORMAL_FILE"
argument_list|)
DECL|field|fileType
specifier|private
name|HdfsFileType
name|fileType
init|=
name|HdfsFileType
operator|.
name|NORMAL_FILE
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"HDFS"
argument_list|)
DECL|field|fileSystemType
specifier|private
name|HdfsFileSystemType
name|fileSystemType
init|=
name|HdfsFileSystemType
operator|.
name|HDFS
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"NULL"
argument_list|)
DECL|field|keyType
specifier|private
name|WritableType
name|keyType
init|=
name|WritableType
operator|.
name|NULL
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"BYTES"
argument_list|)
DECL|field|valueType
specifier|private
name|WritableType
name|valueType
init|=
name|WritableType
operator|.
name|BYTES
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|defaultValue
operator|=
name|HdfsConstants
operator|.
name|DEFAULT_OPENED_SUFFIX
argument_list|)
DECL|field|openedSuffix
specifier|private
name|String
name|openedSuffix
init|=
name|HdfsConstants
operator|.
name|DEFAULT_OPENED_SUFFIX
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|defaultValue
operator|=
name|HdfsConstants
operator|.
name|DEFAULT_READ_SUFFIX
argument_list|)
DECL|field|readSuffix
specifier|private
name|String
name|readSuffix
init|=
name|HdfsConstants
operator|.
name|DEFAULT_READ_SUFFIX
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
name|HdfsConstants
operator|.
name|DEFAULT_PATTERN
argument_list|)
DECL|field|pattern
specifier|private
name|String
name|pattern
init|=
name|HdfsConstants
operator|.
name|DEFAULT_PATTERN
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|defaultValue
operator|=
literal|""
operator|+
name|HdfsConstants
operator|.
name|DEFAULT_BUFFERSIZE
argument_list|)
DECL|field|chunkSize
specifier|private
name|int
name|chunkSize
init|=
name|HdfsConstants
operator|.
name|DEFAULT_BUFFERSIZE
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|defaultValue
operator|=
literal|""
operator|+
name|HdfsConstants
operator|.
name|DEFAULT_CHECK_IDLE_INTERVAL
argument_list|)
DECL|field|checkIdleInterval
specifier|private
name|int
name|checkIdleInterval
init|=
name|HdfsConstants
operator|.
name|DEFAULT_CHECK_IDLE_INTERVAL
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|connectOnStartup
specifier|private
name|boolean
name|connectOnStartup
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
DECL|field|owner
specifier|private
name|String
name|owner
decl_stmt|;
DECL|method|HdfsConfiguration ()
specifier|public
name|HdfsConfiguration
parameter_list|()
block|{     }
DECL|method|getBoolean (Map<String, Object> hdfsSettings, String param, Boolean dflt)
specifier|private
name|Boolean
name|getBoolean
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|hdfsSettings
parameter_list|,
name|String
name|param
parameter_list|,
name|Boolean
name|dflt
parameter_list|)
block|{
if|if
condition|(
name|hdfsSettings
operator|.
name|containsKey
argument_list|(
name|param
argument_list|)
condition|)
block|{
return|return
name|Boolean
operator|.
name|valueOf
argument_list|(
operator|(
name|String
operator|)
name|hdfsSettings
operator|.
name|get
argument_list|(
name|param
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|dflt
return|;
block|}
block|}
DECL|method|getInteger (Map<String, Object> hdfsSettings, String param, Integer dflt)
specifier|private
name|Integer
name|getInteger
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|hdfsSettings
parameter_list|,
name|String
name|param
parameter_list|,
name|Integer
name|dflt
parameter_list|)
block|{
if|if
condition|(
name|hdfsSettings
operator|.
name|containsKey
argument_list|(
name|param
argument_list|)
condition|)
block|{
return|return
name|Integer
operator|.
name|valueOf
argument_list|(
operator|(
name|String
operator|)
name|hdfsSettings
operator|.
name|get
argument_list|(
name|param
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|dflt
return|;
block|}
block|}
DECL|method|getShort (Map<String, Object> hdfsSettings, String param, Short dflt)
specifier|private
name|Short
name|getShort
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|hdfsSettings
parameter_list|,
name|String
name|param
parameter_list|,
name|Short
name|dflt
parameter_list|)
block|{
if|if
condition|(
name|hdfsSettings
operator|.
name|containsKey
argument_list|(
name|param
argument_list|)
condition|)
block|{
return|return
name|Short
operator|.
name|valueOf
argument_list|(
operator|(
name|String
operator|)
name|hdfsSettings
operator|.
name|get
argument_list|(
name|param
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|dflt
return|;
block|}
block|}
DECL|method|getLong (Map<String, Object> hdfsSettings, String param, Long dflt)
specifier|private
name|Long
name|getLong
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|hdfsSettings
parameter_list|,
name|String
name|param
parameter_list|,
name|Long
name|dflt
parameter_list|)
block|{
if|if
condition|(
name|hdfsSettings
operator|.
name|containsKey
argument_list|(
name|param
argument_list|)
condition|)
block|{
return|return
name|Long
operator|.
name|valueOf
argument_list|(
operator|(
name|String
operator|)
name|hdfsSettings
operator|.
name|get
argument_list|(
name|param
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|dflt
return|;
block|}
block|}
DECL|method|getFileType (Map<String, Object> hdfsSettings, String param, HdfsFileType dflt)
specifier|private
name|HdfsFileType
name|getFileType
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|hdfsSettings
parameter_list|,
name|String
name|param
parameter_list|,
name|HdfsFileType
name|dflt
parameter_list|)
block|{
name|String
name|eit
init|=
operator|(
name|String
operator|)
name|hdfsSettings
operator|.
name|get
argument_list|(
name|param
argument_list|)
decl_stmt|;
if|if
condition|(
name|eit
operator|!=
literal|null
condition|)
block|{
return|return
name|HdfsFileType
operator|.
name|valueOf
argument_list|(
name|eit
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|dflt
return|;
block|}
block|}
DECL|method|getFileSystemType (Map<String, Object> hdfsSettings, String param, HdfsFileSystemType dflt)
specifier|private
name|HdfsFileSystemType
name|getFileSystemType
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|hdfsSettings
parameter_list|,
name|String
name|param
parameter_list|,
name|HdfsFileSystemType
name|dflt
parameter_list|)
block|{
name|String
name|eit
init|=
operator|(
name|String
operator|)
name|hdfsSettings
operator|.
name|get
argument_list|(
name|param
argument_list|)
decl_stmt|;
if|if
condition|(
name|eit
operator|!=
literal|null
condition|)
block|{
return|return
name|HdfsFileSystemType
operator|.
name|valueOf
argument_list|(
name|eit
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|dflt
return|;
block|}
block|}
DECL|method|getWritableType (Map<String, Object> hdfsSettings, String param, WritableType dflt)
specifier|private
name|WritableType
name|getWritableType
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|hdfsSettings
parameter_list|,
name|String
name|param
parameter_list|,
name|WritableType
name|dflt
parameter_list|)
block|{
name|String
name|eit
init|=
operator|(
name|String
operator|)
name|hdfsSettings
operator|.
name|get
argument_list|(
name|param
argument_list|)
decl_stmt|;
if|if
condition|(
name|eit
operator|!=
literal|null
condition|)
block|{
return|return
name|WritableType
operator|.
name|valueOf
argument_list|(
name|eit
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|dflt
return|;
block|}
block|}
DECL|method|getCompressionType (Map<String, Object> hdfsSettings, String param, SequenceFile.CompressionType ct)
specifier|private
name|SequenceFile
operator|.
name|CompressionType
name|getCompressionType
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|hdfsSettings
parameter_list|,
name|String
name|param
parameter_list|,
name|SequenceFile
operator|.
name|CompressionType
name|ct
parameter_list|)
block|{
name|String
name|eit
init|=
operator|(
name|String
operator|)
name|hdfsSettings
operator|.
name|get
argument_list|(
name|param
argument_list|)
decl_stmt|;
if|if
condition|(
name|eit
operator|!=
literal|null
condition|)
block|{
return|return
name|SequenceFile
operator|.
name|CompressionType
operator|.
name|valueOf
argument_list|(
name|eit
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|ct
return|;
block|}
block|}
DECL|method|getCompressionCodec (Map<String, Object> hdfsSettings, String param, HdfsCompressionCodec cd)
specifier|private
name|HdfsCompressionCodec
name|getCompressionCodec
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|hdfsSettings
parameter_list|,
name|String
name|param
parameter_list|,
name|HdfsCompressionCodec
name|cd
parameter_list|)
block|{
name|String
name|eit
init|=
operator|(
name|String
operator|)
name|hdfsSettings
operator|.
name|get
argument_list|(
name|param
argument_list|)
decl_stmt|;
if|if
condition|(
name|eit
operator|!=
literal|null
condition|)
block|{
return|return
name|HdfsCompressionCodec
operator|.
name|valueOf
argument_list|(
name|eit
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|cd
return|;
block|}
block|}
DECL|method|getString (Map<String, Object> hdfsSettings, String param, String dflt)
specifier|private
name|String
name|getString
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|hdfsSettings
parameter_list|,
name|String
name|param
parameter_list|,
name|String
name|dflt
parameter_list|)
block|{
if|if
condition|(
name|hdfsSettings
operator|.
name|containsKey
argument_list|(
name|param
argument_list|)
condition|)
block|{
return|return
operator|(
name|String
operator|)
name|hdfsSettings
operator|.
name|get
argument_list|(
name|param
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|dflt
return|;
block|}
block|}
DECL|method|getSplitStrategies (Map<String, Object> hdfsSettings)
specifier|private
name|List
argument_list|<
name|HdfsProducer
operator|.
name|SplitStrategy
argument_list|>
name|getSplitStrategies
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|hdfsSettings
parameter_list|)
block|{
name|List
argument_list|<
name|HdfsProducer
operator|.
name|SplitStrategy
argument_list|>
name|strategies
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|obj
range|:
name|hdfsSettings
operator|.
name|keySet
argument_list|()
control|)
block|{
name|String
name|key
init|=
operator|(
name|String
operator|)
name|obj
decl_stmt|;
if|if
condition|(
literal|"splitStrategy"
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|String
name|eit
init|=
operator|(
name|String
operator|)
name|hdfsSettings
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|eit
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|strstrategies
init|=
name|eit
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|strstrategy
range|:
name|strstrategies
control|)
block|{
name|String
name|tokens
index|[]
init|=
name|strstrategy
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
if|if
condition|(
name|tokens
operator|.
name|length
operator|!=
literal|2
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Wrong Split Strategy "
operator|+
name|key
operator|+
literal|"="
operator|+
name|eit
argument_list|)
throw|;
block|}
name|HdfsProducer
operator|.
name|SplitStrategyType
name|sst
init|=
name|HdfsProducer
operator|.
name|SplitStrategyType
operator|.
name|valueOf
argument_list|(
name|tokens
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|long
name|ssv
init|=
name|Long
operator|.
name|valueOf
argument_list|(
name|tokens
index|[
literal|1
index|]
argument_list|)
decl_stmt|;
name|strategies
operator|.
name|add
argument_list|(
operator|new
name|HdfsProducer
operator|.
name|SplitStrategy
argument_list|(
name|sst
argument_list|,
name|ssv
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|strategies
return|;
block|}
DECL|method|checkConsumerOptions ()
specifier|public
name|void
name|checkConsumerOptions
parameter_list|()
block|{     }
DECL|method|checkProducerOptions ()
specifier|public
name|void
name|checkProducerOptions
parameter_list|()
block|{
if|if
condition|(
name|isAppend
argument_list|()
condition|)
block|{
if|if
condition|(
name|getSplitStrategies
argument_list|()
operator|.
name|size
argument_list|()
operator|!=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Split Strategies incompatible with append=true"
argument_list|)
throw|;
block|}
if|if
condition|(
name|getFileType
argument_list|()
operator|!=
name|HdfsFileType
operator|.
name|NORMAL_FILE
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"append=true works only with NORMAL_FILEs"
argument_list|)
throw|;
block|}
block|}
block|}
DECL|method|parseURI (URI uri)
specifier|public
name|void
name|parseURI
parameter_list|(
name|URI
name|uri
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|String
name|protocol
init|=
name|uri
operator|.
name|getScheme
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|protocol
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"hdfs2"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unrecognized protocol: "
operator|+
name|protocol
operator|+
literal|" for uri: "
operator|+
name|uri
argument_list|)
throw|;
block|}
name|hostName
operator|=
name|uri
operator|.
name|getHost
argument_list|()
expr_stmt|;
if|if
condition|(
name|hostName
operator|==
literal|null
condition|)
block|{
name|hostName
operator|=
literal|"localhost"
expr_stmt|;
block|}
name|port
operator|=
name|uri
operator|.
name|getPort
argument_list|()
operator|==
operator|-
literal|1
condition|?
name|HdfsConstants
operator|.
name|DEFAULT_PORT
else|:
name|uri
operator|.
name|getPort
argument_list|()
expr_stmt|;
name|path
operator|=
name|uri
operator|.
name|getPath
argument_list|()
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|hdfsSettings
init|=
name|URISupport
operator|.
name|parseParameters
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|overwrite
operator|=
name|getBoolean
argument_list|(
name|hdfsSettings
argument_list|,
literal|"overwrite"
argument_list|,
name|overwrite
argument_list|)
expr_stmt|;
name|append
operator|=
name|getBoolean
argument_list|(
name|hdfsSettings
argument_list|,
literal|"append"
argument_list|,
name|append
argument_list|)
expr_stmt|;
name|wantAppend
operator|=
name|append
expr_stmt|;
name|bufferSize
operator|=
name|getInteger
argument_list|(
name|hdfsSettings
argument_list|,
literal|"bufferSize"
argument_list|,
name|bufferSize
argument_list|)
expr_stmt|;
name|replication
operator|=
name|getShort
argument_list|(
name|hdfsSettings
argument_list|,
literal|"replication"
argument_list|,
name|replication
argument_list|)
expr_stmt|;
name|blockSize
operator|=
name|getLong
argument_list|(
name|hdfsSettings
argument_list|,
literal|"blockSize"
argument_list|,
name|blockSize
argument_list|)
expr_stmt|;
name|compressionType
operator|=
name|getCompressionType
argument_list|(
name|hdfsSettings
argument_list|,
literal|"compressionType"
argument_list|,
name|compressionType
argument_list|)
expr_stmt|;
name|compressionCodec
operator|=
name|getCompressionCodec
argument_list|(
name|hdfsSettings
argument_list|,
literal|"compressionCodec"
argument_list|,
name|compressionCodec
argument_list|)
expr_stmt|;
name|fileType
operator|=
name|getFileType
argument_list|(
name|hdfsSettings
argument_list|,
literal|"fileType"
argument_list|,
name|fileType
argument_list|)
expr_stmt|;
name|fileSystemType
operator|=
name|getFileSystemType
argument_list|(
name|hdfsSettings
argument_list|,
literal|"fileSystemType"
argument_list|,
name|fileSystemType
argument_list|)
expr_stmt|;
name|keyType
operator|=
name|getWritableType
argument_list|(
name|hdfsSettings
argument_list|,
literal|"keyType"
argument_list|,
name|keyType
argument_list|)
expr_stmt|;
name|valueType
operator|=
name|getWritableType
argument_list|(
name|hdfsSettings
argument_list|,
literal|"valueType"
argument_list|,
name|valueType
argument_list|)
expr_stmt|;
name|openedSuffix
operator|=
name|getString
argument_list|(
name|hdfsSettings
argument_list|,
literal|"openedSuffix"
argument_list|,
name|openedSuffix
argument_list|)
expr_stmt|;
name|readSuffix
operator|=
name|getString
argument_list|(
name|hdfsSettings
argument_list|,
literal|"readSuffix"
argument_list|,
name|readSuffix
argument_list|)
expr_stmt|;
name|pattern
operator|=
name|getString
argument_list|(
name|hdfsSettings
argument_list|,
literal|"pattern"
argument_list|,
name|pattern
argument_list|)
expr_stmt|;
name|chunkSize
operator|=
name|getInteger
argument_list|(
name|hdfsSettings
argument_list|,
literal|"chunkSize"
argument_list|,
name|chunkSize
argument_list|)
expr_stmt|;
name|splitStrategies
operator|=
name|getSplitStrategies
argument_list|(
name|hdfsSettings
argument_list|)
expr_stmt|;
block|}
DECL|method|getUri ()
specifier|public
name|URI
name|getUri
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
DECL|method|setUri (URI uri)
specifier|public
name|void
name|setUri
parameter_list|(
name|URI
name|uri
parameter_list|)
block|{
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
block|}
DECL|method|getHostName ()
specifier|public
name|String
name|getHostName
parameter_list|()
block|{
return|return
name|hostName
return|;
block|}
comment|/**      * HDFS host to use      */
DECL|method|setHostName (String hostName)
specifier|public
name|void
name|setHostName
parameter_list|(
name|String
name|hostName
parameter_list|)
block|{
name|this
operator|.
name|hostName
operator|=
name|hostName
expr_stmt|;
block|}
DECL|method|getPort ()
specifier|public
name|int
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
comment|/**      * HDFS port to use      */
DECL|method|setPort (int port)
specifier|public
name|void
name|setPort
parameter_list|(
name|int
name|port
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
block|}
DECL|method|getPath ()
specifier|public
name|String
name|getPath
parameter_list|()
block|{
return|return
name|path
return|;
block|}
comment|/**      * The directory path to use      */
DECL|method|setPath (String path)
specifier|public
name|void
name|setPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
block|}
DECL|method|isOverwrite ()
specifier|public
name|boolean
name|isOverwrite
parameter_list|()
block|{
return|return
name|overwrite
return|;
block|}
comment|/**      * Whether to overwrite existing files with the same name      */
DECL|method|setOverwrite (boolean overwrite)
specifier|public
name|void
name|setOverwrite
parameter_list|(
name|boolean
name|overwrite
parameter_list|)
block|{
name|this
operator|.
name|overwrite
operator|=
name|overwrite
expr_stmt|;
block|}
DECL|method|isAppend ()
specifier|public
name|boolean
name|isAppend
parameter_list|()
block|{
return|return
name|append
return|;
block|}
DECL|method|isWantAppend ()
specifier|public
name|boolean
name|isWantAppend
parameter_list|()
block|{
return|return
name|wantAppend
return|;
block|}
comment|/**      * Append to existing file. Notice that not all HDFS file systems support the append option.      */
DECL|method|setAppend (boolean append)
specifier|public
name|void
name|setAppend
parameter_list|(
name|boolean
name|append
parameter_list|)
block|{
name|this
operator|.
name|append
operator|=
name|append
expr_stmt|;
block|}
DECL|method|getBufferSize ()
specifier|public
name|int
name|getBufferSize
parameter_list|()
block|{
return|return
name|bufferSize
return|;
block|}
comment|/**      * The buffer size used by HDFS      */
DECL|method|setBufferSize (int bufferSize)
specifier|public
name|void
name|setBufferSize
parameter_list|(
name|int
name|bufferSize
parameter_list|)
block|{
name|this
operator|.
name|bufferSize
operator|=
name|bufferSize
expr_stmt|;
block|}
DECL|method|getReplication ()
specifier|public
name|short
name|getReplication
parameter_list|()
block|{
return|return
name|replication
return|;
block|}
comment|/**      * The HDFS replication factor      */
DECL|method|setReplication (short replication)
specifier|public
name|void
name|setReplication
parameter_list|(
name|short
name|replication
parameter_list|)
block|{
name|this
operator|.
name|replication
operator|=
name|replication
expr_stmt|;
block|}
DECL|method|getBlockSize ()
specifier|public
name|long
name|getBlockSize
parameter_list|()
block|{
return|return
name|blockSize
return|;
block|}
comment|/**      * The size of the HDFS blocks      */
DECL|method|setBlockSize (long blockSize)
specifier|public
name|void
name|setBlockSize
parameter_list|(
name|long
name|blockSize
parameter_list|)
block|{
name|this
operator|.
name|blockSize
operator|=
name|blockSize
expr_stmt|;
block|}
DECL|method|getFileType ()
specifier|public
name|HdfsFileType
name|getFileType
parameter_list|()
block|{
return|return
name|fileType
return|;
block|}
comment|/**      * The file type to use. For more details see Hadoop HDFS documentation about the various files types.      */
DECL|method|setFileType (HdfsFileType fileType)
specifier|public
name|void
name|setFileType
parameter_list|(
name|HdfsFileType
name|fileType
parameter_list|)
block|{
name|this
operator|.
name|fileType
operator|=
name|fileType
expr_stmt|;
block|}
DECL|method|getCompressionType ()
specifier|public
name|SequenceFile
operator|.
name|CompressionType
name|getCompressionType
parameter_list|()
block|{
return|return
name|compressionType
return|;
block|}
comment|/**      * The compression type to use (is default not in use)      */
DECL|method|setCompressionType (SequenceFile.CompressionType compressionType)
specifier|public
name|void
name|setCompressionType
parameter_list|(
name|SequenceFile
operator|.
name|CompressionType
name|compressionType
parameter_list|)
block|{
name|this
operator|.
name|compressionType
operator|=
name|compressionType
expr_stmt|;
block|}
DECL|method|getCompressionCodec ()
specifier|public
name|HdfsCompressionCodec
name|getCompressionCodec
parameter_list|()
block|{
return|return
name|compressionCodec
return|;
block|}
comment|/**      * The compression codec to use      */
DECL|method|setCompressionCodec (HdfsCompressionCodec compressionCodec)
specifier|public
name|void
name|setCompressionCodec
parameter_list|(
name|HdfsCompressionCodec
name|compressionCodec
parameter_list|)
block|{
name|this
operator|.
name|compressionCodec
operator|=
name|compressionCodec
expr_stmt|;
block|}
comment|/**      * Set to LOCAL to not use HDFS but local java.io.File instead.      */
DECL|method|setFileSystemType (HdfsFileSystemType fileSystemType)
specifier|public
name|void
name|setFileSystemType
parameter_list|(
name|HdfsFileSystemType
name|fileSystemType
parameter_list|)
block|{
name|this
operator|.
name|fileSystemType
operator|=
name|fileSystemType
expr_stmt|;
block|}
DECL|method|getFileSystemType ()
specifier|public
name|HdfsFileSystemType
name|getFileSystemType
parameter_list|()
block|{
return|return
name|fileSystemType
return|;
block|}
DECL|method|getKeyType ()
specifier|public
name|WritableType
name|getKeyType
parameter_list|()
block|{
return|return
name|keyType
return|;
block|}
comment|/**      * The type for the key in case of sequence or map files.      */
DECL|method|setKeyType (WritableType keyType)
specifier|public
name|void
name|setKeyType
parameter_list|(
name|WritableType
name|keyType
parameter_list|)
block|{
name|this
operator|.
name|keyType
operator|=
name|keyType
expr_stmt|;
block|}
DECL|method|getValueType ()
specifier|public
name|WritableType
name|getValueType
parameter_list|()
block|{
return|return
name|valueType
return|;
block|}
comment|/**      * The type for the key in case of sequence or map files      */
DECL|method|setValueType (WritableType valueType)
specifier|public
name|void
name|setValueType
parameter_list|(
name|WritableType
name|valueType
parameter_list|)
block|{
name|this
operator|.
name|valueType
operator|=
name|valueType
expr_stmt|;
block|}
comment|/**      * When a file is opened for reading/writing the file is renamed with this suffix to avoid to read it during the writing phase.      */
DECL|method|setOpenedSuffix (String openedSuffix)
specifier|public
name|void
name|setOpenedSuffix
parameter_list|(
name|String
name|openedSuffix
parameter_list|)
block|{
name|this
operator|.
name|openedSuffix
operator|=
name|openedSuffix
expr_stmt|;
block|}
DECL|method|getOpenedSuffix ()
specifier|public
name|String
name|getOpenedSuffix
parameter_list|()
block|{
return|return
name|openedSuffix
return|;
block|}
comment|/**      * Once the file has been read is renamed with this suffix to avoid to read it again.      */
DECL|method|setReadSuffix (String readSuffix)
specifier|public
name|void
name|setReadSuffix
parameter_list|(
name|String
name|readSuffix
parameter_list|)
block|{
name|this
operator|.
name|readSuffix
operator|=
name|readSuffix
expr_stmt|;
block|}
DECL|method|getReadSuffix ()
specifier|public
name|String
name|getReadSuffix
parameter_list|()
block|{
return|return
name|readSuffix
return|;
block|}
comment|/**      * The pattern used for scanning the directory      */
DECL|method|setPattern (String pattern)
specifier|public
name|void
name|setPattern
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|this
operator|.
name|pattern
operator|=
name|pattern
expr_stmt|;
block|}
DECL|method|getPattern ()
specifier|public
name|String
name|getPattern
parameter_list|()
block|{
return|return
name|pattern
return|;
block|}
comment|/**      * When reading a normal file, this is split into chunks producing a message per chunk.      */
DECL|method|setChunkSize (int chunkSize)
specifier|public
name|void
name|setChunkSize
parameter_list|(
name|int
name|chunkSize
parameter_list|)
block|{
name|this
operator|.
name|chunkSize
operator|=
name|chunkSize
expr_stmt|;
block|}
DECL|method|getChunkSize ()
specifier|public
name|int
name|getChunkSize
parameter_list|()
block|{
return|return
name|chunkSize
return|;
block|}
comment|/**      * How often (time in millis) in to run the idle checker background task. This option is only in use if the splitter strategy is IDLE.      */
DECL|method|setCheckIdleInterval (int checkIdleInterval)
specifier|public
name|void
name|setCheckIdleInterval
parameter_list|(
name|int
name|checkIdleInterval
parameter_list|)
block|{
name|this
operator|.
name|checkIdleInterval
operator|=
name|checkIdleInterval
expr_stmt|;
block|}
DECL|method|getCheckIdleInterval ()
specifier|public
name|int
name|getCheckIdleInterval
parameter_list|()
block|{
return|return
name|checkIdleInterval
return|;
block|}
DECL|method|getSplitStrategies ()
specifier|public
name|List
argument_list|<
name|HdfsProducer
operator|.
name|SplitStrategy
argument_list|>
name|getSplitStrategies
parameter_list|()
block|{
return|return
name|splitStrategies
return|;
block|}
DECL|method|getSplitStrategy ()
specifier|public
name|String
name|getSplitStrategy
parameter_list|()
block|{
return|return
name|splitStrategy
return|;
block|}
comment|/**      * In the current version of Hadoop opening a file in append mode is disabled since it's not very reliable. So, for the moment,      * it's only possible to create new files. The Camel HDFS endpoint tries to solve this problem in this way:      *<ul>      *<li>If the split strategy option has been defined, the hdfs path will be used as a directory and files will be created using the configured UuidGenerator.</li>      *<li>Every time a splitting condition is met, a new file is created.</li>      *</ul>      * The splitStrategy option is defined as a string with the following syntax:      *<br/><tt>splitStrategy=ST:value,ST:value,...</tt>      *<br/>where ST can be:      *<ul>      *<li>BYTES a new file is created, and the old is closed when the number of written bytes is more than value</li>      *<li>MESSAGES a new file is created, and the old is closed when the number of written messages is more than value</li>      *<li>IDLE a new file is created, and the old is closed when no writing happened in the last value milliseconds</li>      *</ul>      */
DECL|method|setSplitStrategy (String splitStrategy)
specifier|public
name|void
name|setSplitStrategy
parameter_list|(
name|String
name|splitStrategy
parameter_list|)
block|{
name|this
operator|.
name|splitStrategy
operator|=
name|splitStrategy
expr_stmt|;
block|}
DECL|method|isConnectOnStartup ()
specifier|public
name|boolean
name|isConnectOnStartup
parameter_list|()
block|{
return|return
name|connectOnStartup
return|;
block|}
comment|/**      * Whether to connect to the HDFS file system on starting the producer/consumer.      * If false then the connection is created on-demand. Notice that HDFS may take up till 15 minutes to establish      * a connection, as it has hardcoded 45 x 20 sec redelivery. By setting this option to false allows your      * application to startup, and not block for up till 15 minutes.      */
DECL|method|setConnectOnStartup (boolean connectOnStartup)
specifier|public
name|void
name|setConnectOnStartup
parameter_list|(
name|boolean
name|connectOnStartup
parameter_list|)
block|{
name|this
operator|.
name|connectOnStartup
operator|=
name|connectOnStartup
expr_stmt|;
block|}
DECL|method|getOwner ()
specifier|public
name|String
name|getOwner
parameter_list|()
block|{
return|return
name|owner
return|;
block|}
comment|/**      * The file owner must match this owner for the consumer to pickup the file. Otherwise the file is skipped.      */
DECL|method|setOwner (String owner)
specifier|public
name|void
name|setOwner
parameter_list|(
name|String
name|owner
parameter_list|)
block|{
name|this
operator|.
name|owner
operator|=
name|owner
expr_stmt|;
block|}
block|}
end_class

end_unit

