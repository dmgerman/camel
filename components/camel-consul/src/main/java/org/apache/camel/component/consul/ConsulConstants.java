begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.consul
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|consul
package|;
end_package

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|Consul
import|;
end_import

begin_interface
DECL|interface|ConsulConstants
specifier|public
interface|interface
name|ConsulConstants
block|{
DECL|field|CONSUL_DEFAULT_URL
name|String
name|CONSUL_DEFAULT_URL
init|=
name|String
operator|.
name|format
argument_list|(
literal|"http://%s:%d"
argument_list|,
name|Consul
operator|.
name|DEFAULT_HTTP_HOST
argument_list|,
name|Consul
operator|.
name|DEFAULT_HTTP_PORT
argument_list|)
decl_stmt|;
comment|// Service Call EIP
DECL|field|CONSUL_SERVER_IP
name|String
name|CONSUL_SERVER_IP
init|=
literal|"CamelConsulServerIp"
decl_stmt|;
DECL|field|CONSUL_SERVER_PORT
name|String
name|CONSUL_SERVER_PORT
init|=
literal|"CamelConsulServerPort"
decl_stmt|;
DECL|field|CONSUL_ACTION
name|String
name|CONSUL_ACTION
init|=
literal|"CamelConsulAction"
decl_stmt|;
DECL|field|CONSUL_KEY
name|String
name|CONSUL_KEY
init|=
literal|"CamelConsulKey"
decl_stmt|;
DECL|field|CONSUL_EVENT_ID
name|String
name|CONSUL_EVENT_ID
init|=
literal|"CamelConsulEventId"
decl_stmt|;
DECL|field|CONSUL_EVENT_NAME
name|String
name|CONSUL_EVENT_NAME
init|=
literal|"CamelConsulEventName"
decl_stmt|;
DECL|field|CONSUL_EVENT_LTIME
name|String
name|CONSUL_EVENT_LTIME
init|=
literal|"CamelConsulEventLTime"
decl_stmt|;
DECL|field|CONSUL_NODE_FILTER
name|String
name|CONSUL_NODE_FILTER
init|=
literal|"CamelConsulNodeFilter"
decl_stmt|;
DECL|field|CONSUL_TAG_FILTER
name|String
name|CONSUL_TAG_FILTER
init|=
literal|"CamelConsulTagFilter"
decl_stmt|;
DECL|field|CONSUL_SERVICE_FILTER
name|String
name|CONSUL_SERVICE_FILTER
init|=
literal|"CamelConsulSessionFilter"
decl_stmt|;
DECL|field|CONSUL_VERSION
name|String
name|CONSUL_VERSION
init|=
literal|"CamelConsulVersion"
decl_stmt|;
DECL|field|CONSUL_FLAGS
name|String
name|CONSUL_FLAGS
init|=
literal|"CamelConsulFlags"
decl_stmt|;
DECL|field|CONSUL_CREATE_INDEX
name|String
name|CONSUL_CREATE_INDEX
init|=
literal|"CamelConsulCreateIndex"
decl_stmt|;
DECL|field|CONSUL_LOCK_INDEX
name|String
name|CONSUL_LOCK_INDEX
init|=
literal|"CamelConsulLockIndex"
decl_stmt|;
DECL|field|CONSUL_MODIFY_INDEX
name|String
name|CONSUL_MODIFY_INDEX
init|=
literal|"CamelConsulModifyIndex"
decl_stmt|;
DECL|field|CONSUL_OPTIONS
name|String
name|CONSUL_OPTIONS
init|=
literal|"CamelConsulOptions"
decl_stmt|;
DECL|field|CONSUL_RESULT
name|String
name|CONSUL_RESULT
init|=
literal|"CamelConsulResult"
decl_stmt|;
DECL|field|CONSUL_SESSION
name|String
name|CONSUL_SESSION
init|=
literal|"CamelConsulSession"
decl_stmt|;
DECL|field|CONSUL_VALUE_AS_STRING
name|String
name|CONSUL_VALUE_AS_STRING
init|=
literal|"CamelConsulValueAsString"
decl_stmt|;
block|}
end_interface

end_unit

