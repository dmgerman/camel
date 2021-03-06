begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.lumberjack.io
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|lumberjack
operator|.
name|io
package|;
end_package

begin_class
DECL|class|LumberjackConstants
specifier|final
class|class
name|LumberjackConstants
block|{
DECL|field|VERSION_V1
specifier|static
specifier|final
name|int
name|VERSION_V1
init|=
literal|'1'
decl_stmt|;
DECL|field|VERSION_V2
specifier|static
specifier|final
name|int
name|VERSION_V2
init|=
literal|'2'
decl_stmt|;
DECL|field|TYPE_ACKNOWLEDGE
specifier|static
specifier|final
name|int
name|TYPE_ACKNOWLEDGE
init|=
literal|'A'
decl_stmt|;
DECL|field|TYPE_WINDOW
specifier|static
specifier|final
name|int
name|TYPE_WINDOW
init|=
literal|'W'
decl_stmt|;
DECL|field|TYPE_COMPRESS
specifier|static
specifier|final
name|int
name|TYPE_COMPRESS
init|=
literal|'C'
decl_stmt|;
DECL|field|TYPE_JSON
specifier|static
specifier|final
name|int
name|TYPE_JSON
init|=
literal|'J'
decl_stmt|;
DECL|field|TYPE_DATA
specifier|static
specifier|final
name|int
name|TYPE_DATA
init|=
literal|'D'
decl_stmt|;
DECL|field|INT_LENGTH
specifier|static
specifier|final
name|int
name|INT_LENGTH
init|=
literal|4
decl_stmt|;
DECL|field|FRAME_HEADER_LENGTH
specifier|static
specifier|final
name|int
name|FRAME_HEADER_LENGTH
init|=
literal|1
operator|+
literal|1
decl_stmt|;
comment|// version(byte) + type(byte)
DECL|field|FRAME_ACKNOWLEDGE_LENGTH
specifier|static
specifier|final
name|int
name|FRAME_ACKNOWLEDGE_LENGTH
init|=
name|FRAME_HEADER_LENGTH
operator|+
name|INT_LENGTH
decl_stmt|;
comment|// sequence number(int)
DECL|field|FRAME_JSON_HEADER_LENGTH
specifier|static
specifier|final
name|int
name|FRAME_JSON_HEADER_LENGTH
init|=
name|INT_LENGTH
operator|+
name|INT_LENGTH
decl_stmt|;
comment|// sequence number(int) + payload length(int)
DECL|field|FRAME_DATA_HEADER_LENGTH
specifier|static
specifier|final
name|int
name|FRAME_DATA_HEADER_LENGTH
init|=
name|INT_LENGTH
operator|+
name|INT_LENGTH
decl_stmt|;
comment|// sequence number(int) + key/value pair count(int)
DECL|field|FRAME_WINDOW_HEADER_LENGTH
specifier|static
specifier|final
name|int
name|FRAME_WINDOW_HEADER_LENGTH
init|=
name|INT_LENGTH
decl_stmt|;
comment|// window size(int)
DECL|field|FRAME_COMPRESS_HEADER_LENGTH
specifier|static
specifier|final
name|int
name|FRAME_COMPRESS_HEADER_LENGTH
init|=
name|INT_LENGTH
decl_stmt|;
comment|// compressed payload length(int)
DECL|method|LumberjackConstants ()
specifier|private
name|LumberjackConstants
parameter_list|()
block|{     }
block|}
end_class

end_unit

