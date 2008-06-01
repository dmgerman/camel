begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.wsdl_first
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|wsdl_first
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebService
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Holder
import|;
end_import

begin_class
annotation|@
name|WebService
argument_list|(
name|serviceName
operator|=
literal|"PersonService"
argument_list|,
name|targetNamespace
operator|=
literal|"http://camel.apache.org/wsdl-first"
argument_list|,
name|endpointInterface
operator|=
literal|"org.apache.camel.wsdl_first.Person"
argument_list|)
DECL|class|PersonImpl
specifier|public
class|class
name|PersonImpl
implements|implements
name|Person
block|{
DECL|method|getPerson (Holder<String> personId, Holder<String> ssn, Holder<String> name)
specifier|public
name|void
name|getPerson
parameter_list|(
name|Holder
argument_list|<
name|String
argument_list|>
name|personId
parameter_list|,
name|Holder
argument_list|<
name|String
argument_list|>
name|ssn
parameter_list|,
name|Holder
argument_list|<
name|String
argument_list|>
name|name
parameter_list|)
throws|throws
name|UnknownPersonFault
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"the server is invoked "
argument_list|)
expr_stmt|;
if|if
condition|(
name|personId
operator|.
name|value
operator|==
literal|null
operator|||
name|personId
operator|.
name|value
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|wsdl_first
operator|.
name|types
operator|.
name|UnknownPersonFault
name|fault
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|wsdl_first
operator|.
name|types
operator|.
name|UnknownPersonFault
argument_list|()
decl_stmt|;
name|fault
operator|.
name|setPersonId
argument_list|(
name|personId
operator|.
name|value
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|UnknownPersonFault
argument_list|(
literal|null
argument_list|,
name|fault
argument_list|)
throw|;
block|}
name|name
operator|.
name|value
operator|=
literal|"Bonjour"
expr_stmt|;
name|ssn
operator|.
name|value
operator|=
literal|"000-000-0000"
expr_stmt|;
block|}
block|}
end_class

end_unit

