begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.thrift
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|thrift
package|;
end_package

begin_comment
comment|/**  * Thrift component constants  */
end_comment

begin_interface
DECL|interface|ThriftConstants
specifier|public
interface|interface
name|ThriftConstants
block|{
DECL|field|THRIFT_SYNC_CLIENT_CLASS_NAME
name|String
name|THRIFT_SYNC_CLIENT_CLASS_NAME
init|=
literal|"Client"
decl_stmt|;
DECL|field|THRIFT_ASYNC_CLIENT_CLASS_NAME
name|String
name|THRIFT_ASYNC_CLIENT_CLASS_NAME
init|=
literal|"AsyncClient"
decl_stmt|;
DECL|field|THRIFT_ASYNC_CLIENT_FACTORY_NAME
name|String
name|THRIFT_ASYNC_CLIENT_FACTORY_NAME
init|=
literal|"Factory"
decl_stmt|;
DECL|field|THRIFT_ASYNC_CLIENT_GETTER_NAME
name|String
name|THRIFT_ASYNC_CLIENT_GETTER_NAME
init|=
literal|"getAsyncClient"
decl_stmt|;
DECL|field|THRIFT_SERVER_SYNC_INTERFACE_NAME
name|String
name|THRIFT_SERVER_SYNC_INTERFACE_NAME
init|=
literal|"Iface"
decl_stmt|;
DECL|field|THRIFT_SERVER_SYNC_PROCESSOR_CLASS
name|String
name|THRIFT_SERVER_SYNC_PROCESSOR_CLASS
init|=
literal|"Processor"
decl_stmt|;
DECL|field|THRIFT_SERVER_ASYNC_INTERFACE_NAME
name|String
name|THRIFT_SERVER_ASYNC_INTERFACE_NAME
init|=
literal|"AsyncIface"
decl_stmt|;
DECL|field|THRIFT_SERVER_ASYNC_PROCESSOR_CLASS
name|String
name|THRIFT_SERVER_ASYNC_PROCESSOR_CLASS
init|=
literal|"AsyncProcessor"
decl_stmt|;
DECL|field|THRIFT_CONSUMER_POOL_SIZE
name|int
name|THRIFT_CONSUMER_POOL_SIZE
init|=
literal|1
decl_stmt|;
DECL|field|THRIFT_CONSUMER_MAX_POOL_SIZE
name|int
name|THRIFT_CONSUMER_MAX_POOL_SIZE
init|=
literal|10
decl_stmt|;
comment|/*      * This headers will be set after Thrift consumer method is invoked      */
DECL|field|THRIFT_METHOD_NAME_HEADER
name|String
name|THRIFT_METHOD_NAME_HEADER
init|=
literal|"CamelThriftMethodName"
decl_stmt|;
block|}
end_interface

end_unit

