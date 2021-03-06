begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cloud
package|;
end_package

begin_interface
DECL|interface|ServiceCallConstants
specifier|public
interface|interface
name|ServiceCallConstants
block|{
DECL|field|SERVICE_CALL_SCHEME
name|String
name|SERVICE_CALL_SCHEME
init|=
literal|"CamelServiceCallScheme"
decl_stmt|;
DECL|field|SERVICE_CALL_URI
name|String
name|SERVICE_CALL_URI
init|=
literal|"CamelServiceCallUri"
decl_stmt|;
DECL|field|SERVICE_CALL_CONTEXT_PATH
name|String
name|SERVICE_CALL_CONTEXT_PATH
init|=
literal|"CamelServiceCallContextPath"
decl_stmt|;
DECL|field|SERVICE_NAME
name|String
name|SERVICE_NAME
init|=
literal|"CamelServiceCallServiceName"
decl_stmt|;
DECL|field|SERVICE_META
name|String
name|SERVICE_META
init|=
literal|"CamelServiceCallServiceMeta"
decl_stmt|;
DECL|field|SERVICE_HOST
name|String
name|SERVICE_HOST
init|=
literal|"CamelServiceCallServiceHost"
decl_stmt|;
DECL|field|SERVICE_PORT
name|String
name|SERVICE_PORT
init|=
literal|"CamelServiceCallServicePort"
decl_stmt|;
block|}
end_interface

end_unit

