begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.camel.transport
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|camel
operator|.
name|transport
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|Greeter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|PingMeFault
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|types
operator|.
name|FaultDetail
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
name|portName
operator|=
literal|"CamelPort"
argument_list|,
name|serviceName
operator|=
literal|"CamelService"
argument_list|,
name|targetNamespace
operator|=
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
name|endpointInterface
operator|=
literal|"org.apache.hello_world_soap_http.Greeter"
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
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|GreeterImpl
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|suffix
specifier|private
name|String
name|suffix
decl_stmt|;
DECL|method|GreeterImpl ()
specifier|public
name|GreeterImpl
parameter_list|()
block|{     }
DECL|method|setSuffix (String suffix)
specifier|public
name|void
name|setSuffix
parameter_list|(
name|String
name|suffix
parameter_list|)
block|{
name|this
operator|.
name|suffix
operator|=
name|suffix
expr_stmt|;
block|}
comment|/* (non-Javadoc)      * @see org.apache.hello_world_soap_http.Greeter#greetMe(java.lang.String)      */
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
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Executing operation greetMe"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Message received: "
operator|+
name|me
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
return|return
literal|"Hello "
operator|+
name|me
operator|+
literal|" from "
operator|+
name|suffix
return|;
block|}
comment|/* (non-Javadoc)      * @see org.apache.hello_world_soap_http.Greeter#greetMeOneWay(java.lang.String)      */
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
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Executing operation greetMeOneWay\n"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Hello there "
operator|+
name|me
argument_list|)
expr_stmt|;
block|}
comment|/* (non-Javadoc)      * @see org.apache.hello_world_soap_http.Greeter#sayHi()      */
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
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Executing operation sayHi\n"
argument_list|)
expr_stmt|;
return|return
literal|"Bonjour from "
operator|+
name|suffix
return|;
block|}
DECL|method|pingMe (String messageIn)
specifier|public
name|void
name|pingMe
parameter_list|(
name|String
name|messageIn
parameter_list|)
throws|throws
name|PingMeFault
block|{
name|FaultDetail
name|faultDetail
init|=
operator|new
name|FaultDetail
argument_list|()
decl_stmt|;
name|faultDetail
operator|.
name|setMajor
argument_list|(
operator|(
name|short
operator|)
literal|2
argument_list|)
expr_stmt|;
name|faultDetail
operator|.
name|setMinor
argument_list|(
operator|(
name|short
operator|)
literal|1
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Executing operation pingMe, throwing PingMeFault exception, message = "
operator|+
name|messageIn
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Executing operation pingMe, throwing PingMeFault exception\n"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|PingMeFault
argument_list|(
literal|"PingMeFault raised by server "
operator|+
name|suffix
argument_list|,
name|faultDetail
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

