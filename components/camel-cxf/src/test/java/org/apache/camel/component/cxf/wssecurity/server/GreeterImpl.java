begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.wssecurity.server
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
operator|.
name|wssecurity
operator|.
name|server
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|hello_world_soap_http
operator|.
name|Greeter
import|;
end_import

begin_class
annotation|@
name|javax
operator|.
name|jws
operator|.
name|WebService
argument_list|(
name|serviceName
operator|=
literal|"GreeterService"
argument_list|,
name|portName
operator|=
literal|"GreeterPort"
argument_list|,
name|endpointInterface
operator|=
literal|"org.apache.camel.hello_world_soap_http.Greeter"
argument_list|,
name|targetNamespace
operator|=
literal|"http://camel.apache.org/hello_world_soap_http"
argument_list|)
DECL|class|GreeterImpl
specifier|public
class|class
name|GreeterImpl
implements|implements
name|Greeter
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|GreeterImpl
operator|.
name|class
operator|.
name|getPackage
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
comment|/* (non-Javadoc)      * @see org.apache.cxf.hello_world_soap_http.Greeter#greetMe(java.lang.String)      */
DECL|method|greetMe (String me)
specifier|public
name|String
name|greetMe
parameter_list|(
name|String
name|me
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Executing operation greetMe"
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Message received: "
operator|+
name|me
argument_list|)
expr_stmt|;
return|return
literal|"Hello "
operator|+
name|me
return|;
block|}
comment|/* (non-Javadoc)      * @see org.apache.cxf.hello_world_soap_http.Greeter#greetMeOneWay(java.lang.String)      */
DECL|method|greetMeOneWay (String me)
specifier|public
name|void
name|greetMeOneWay
parameter_list|(
name|String
name|me
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Executing operation greetMeOneWay"
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Hello there "
operator|+
name|me
argument_list|)
expr_stmt|;
block|}
comment|/* (non-Javadoc)      * @see org.apache.cxf.hello_world_soap_http.Greeter#sayHi()      */
DECL|method|sayHi ()
specifier|public
name|String
name|sayHi
parameter_list|()
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Executing operation sayHi"
argument_list|)
expr_stmt|;
return|return
literal|"Bonjour"
return|;
block|}
block|}
end_class

end_unit

