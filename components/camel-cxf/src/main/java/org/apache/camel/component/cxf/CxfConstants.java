begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
package|;
end_package

begin_comment
comment|/**  * Constants used in this module  *  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|CxfConstants
specifier|public
interface|interface
name|CxfConstants
block|{
DECL|field|METHOD
name|String
name|METHOD
init|=
literal|"method"
decl_stmt|;
DECL|field|SEI
name|String
name|SEI
init|=
literal|"sei"
decl_stmt|;
DECL|field|IMPL
name|String
name|IMPL
init|=
literal|"impl"
decl_stmt|;
DECL|field|WSDL_URL
name|String
name|WSDL_URL
init|=
literal|"wsdlURL"
decl_stmt|;
DECL|field|ADDRESS
name|String
name|ADDRESS
init|=
literal|"address"
decl_stmt|;
DECL|field|SERVICE_NAME
name|String
name|SERVICE_NAME
init|=
literal|"serviceName"
decl_stmt|;
DECL|field|PORT_NAME
name|String
name|PORT_NAME
init|=
literal|"portName"
decl_stmt|;
comment|// service name -- come from the wsdl
block|}
end_interface

end_unit

