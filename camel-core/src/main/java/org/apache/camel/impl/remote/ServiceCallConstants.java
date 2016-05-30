begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.remote
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|remote
package|;
end_package

begin_interface
DECL|interface|ServiceCallConstants
specifier|public
interface|interface
name|ServiceCallConstants
block|{
DECL|field|SERVICE_NAME
name|String
name|SERVICE_NAME
init|=
literal|"CamelServiceCallServiceNa,e"
decl_stmt|;
DECL|field|SERVER_IP
name|String
name|SERVER_IP
init|=
literal|"CamelServiceCallServerIp"
decl_stmt|;
DECL|field|SERVER_PORT
name|String
name|SERVER_PORT
init|=
literal|"CamelServiceCallServerPort"
decl_stmt|;
block|}
end_interface

end_unit

