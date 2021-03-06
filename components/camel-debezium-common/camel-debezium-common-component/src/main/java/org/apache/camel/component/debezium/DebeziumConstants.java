begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.debezium
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|debezium
package|;
end_package

begin_import
import|import
name|io
operator|.
name|debezium
operator|.
name|relational
operator|.
name|history
operator|.
name|FileDatabaseHistory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|connect
operator|.
name|storage
operator|.
name|FileOffsetBackingStore
import|;
end_import

begin_class
DECL|class|DebeziumConstants
specifier|public
specifier|final
class|class
name|DebeziumConstants
block|{
comment|// embedded engine constant
DECL|field|DEFAULT_OFFSET_STORAGE
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_OFFSET_STORAGE
init|=
name|FileOffsetBackingStore
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|// mysql constant
DECL|field|DEFAULT_DATABASE_HISTORY
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_DATABASE_HISTORY
init|=
name|FileDatabaseHistory
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|// header names
DECL|field|HEADER_PREFIX
specifier|private
specifier|static
specifier|final
name|String
name|HEADER_PREFIX
init|=
literal|"CamelDebezium"
decl_stmt|;
DECL|field|HEADER_SOURCE_METADATA
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_SOURCE_METADATA
init|=
name|HEADER_PREFIX
operator|+
literal|"SourceMetadata"
decl_stmt|;
DECL|field|HEADER_IDENTIFIER
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_IDENTIFIER
init|=
name|HEADER_PREFIX
operator|+
literal|"Identifier"
decl_stmt|;
DECL|field|HEADER_KEY
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_KEY
init|=
name|HEADER_PREFIX
operator|+
literal|"Key"
decl_stmt|;
DECL|field|HEADER_OPERATION
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_OPERATION
init|=
name|HEADER_PREFIX
operator|+
literal|"Operation"
decl_stmt|;
DECL|field|HEADER_TIMESTAMP
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_TIMESTAMP
init|=
name|HEADER_PREFIX
operator|+
literal|"Timestamp"
decl_stmt|;
DECL|field|HEADER_BEFORE
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_BEFORE
init|=
name|HEADER_PREFIX
operator|+
literal|"Before"
decl_stmt|;
DECL|method|DebeziumConstants ()
specifier|private
name|DebeziumConstants
parameter_list|()
block|{     }
block|}
end_class

end_unit

