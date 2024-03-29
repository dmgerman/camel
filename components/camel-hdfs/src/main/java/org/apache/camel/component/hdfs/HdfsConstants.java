begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hdfs
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hdfs
package|;
end_package

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
DECL|class|HdfsConstants
specifier|public
specifier|final
class|class
name|HdfsConstants
block|{
DECL|field|DEFAULT_PORT
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_PORT
init|=
literal|8020
decl_stmt|;
DECL|field|DEFAULT_BUFFERSIZE
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_BUFFERSIZE
init|=
literal|4096
decl_stmt|;
DECL|field|DEFAULT_REPLICATION
specifier|public
specifier|static
specifier|final
name|short
name|DEFAULT_REPLICATION
init|=
literal|3
decl_stmt|;
DECL|field|DEFAULT_BLOCKSIZE
specifier|public
specifier|static
specifier|final
name|long
name|DEFAULT_BLOCKSIZE
init|=
literal|64
operator|*
literal|1024
operator|*
literal|1024L
decl_stmt|;
DECL|field|DEFAULT_COMPRESSIONTYPE
specifier|public
specifier|static
specifier|final
name|SequenceFile
operator|.
name|CompressionType
name|DEFAULT_COMPRESSIONTYPE
init|=
name|SequenceFile
operator|.
name|CompressionType
operator|.
name|NONE
decl_stmt|;
DECL|field|DEFAULT_CODEC
specifier|public
specifier|static
specifier|final
name|HdfsCompressionCodec
name|DEFAULT_CODEC
init|=
name|HdfsCompressionCodec
operator|.
name|DEFAULT
decl_stmt|;
DECL|field|DEFAULT_OPENED_SUFFIX
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_OPENED_SUFFIX
init|=
literal|"opened"
decl_stmt|;
DECL|field|DEFAULT_READ_SUFFIX
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_READ_SUFFIX
init|=
literal|"read"
decl_stmt|;
DECL|field|DEFAULT_SEGMENT_PREFIX
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_SEGMENT_PREFIX
init|=
literal|"seg"
decl_stmt|;
DECL|field|DEFAULT_DELAY
specifier|public
specifier|static
specifier|final
name|long
name|DEFAULT_DELAY
init|=
literal|1000L
decl_stmt|;
DECL|field|DEFAULT_PATTERN
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_PATTERN
init|=
literal|"*"
decl_stmt|;
DECL|field|DEFAULT_CHECK_IDLE_INTERVAL
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_CHECK_IDLE_INTERVAL
init|=
literal|500
decl_stmt|;
DECL|field|HDFS_CLOSE
specifier|public
specifier|static
specifier|final
name|String
name|HDFS_CLOSE
init|=
literal|"CamelHdfsClose"
decl_stmt|;
DECL|field|DEFAULT_MAX_MESSAGES_PER_POLL
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_MAX_MESSAGES_PER_POLL
init|=
literal|100
decl_stmt|;
DECL|method|HdfsConstants ()
specifier|private
name|HdfsConstants
parameter_list|()
block|{     }
block|}
end_class

end_unit

