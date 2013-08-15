begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.avro
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|avro
package|;
end_package

begin_class
DECL|class|AvroConstants
specifier|public
specifier|final
class|class
name|AvroConstants
block|{
DECL|field|AVRO_NETTY_TRANSPORT
specifier|public
specifier|static
specifier|final
name|String
name|AVRO_NETTY_TRANSPORT
init|=
literal|"netty"
decl_stmt|;
DECL|field|AVRO_HTTP_TRANSPORT
specifier|public
specifier|static
specifier|final
name|String
name|AVRO_HTTP_TRANSPORT
init|=
literal|"http"
decl_stmt|;
DECL|field|AVRO_MESSAGE_NAME_SEPARATOR
specifier|public
specifier|static
specifier|final
name|String
name|AVRO_MESSAGE_NAME_SEPARATOR
init|=
literal|"/"
decl_stmt|;
DECL|field|AVRO_MESSAGE_NAME
specifier|public
specifier|static
specifier|final
name|String
name|AVRO_MESSAGE_NAME
init|=
literal|"CamelAvroMessageName"
decl_stmt|;
DECL|method|AvroConstants ()
specifier|private
name|AvroConstants
parameter_list|()
block|{
comment|// Utility class
block|}
block|}
end_class

end_unit

