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
name|xml
operator|.
name|ws
operator|.
name|Holder
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
name|Exchange
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
name|Processor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|MessageContentsList
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|model
operator|.
name|BindingOperationInfo
import|;
end_import

begin_comment
comment|// START SNIPPET: personProcessor
end_comment

begin_class
DECL|class|PersonProcessor
specifier|public
class|class
name|PersonProcessor
implements|implements
name|Processor
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|PersonProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"processing exchange in camel"
argument_list|)
expr_stmt|;
name|BindingOperationInfo
name|boi
init|=
operator|(
name|BindingOperationInfo
operator|)
name|exchange
operator|.
name|getProperty
argument_list|(
name|BindingOperationInfo
operator|.
name|class
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|boi
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"boi.isUnwrapped"
operator|+
name|boi
operator|.
name|isUnwrapped
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Get the parameters list which element is the holder.
name|MessageContentsList
name|msgList
init|=
operator|(
name|MessageContentsList
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|Holder
argument_list|<
name|String
argument_list|>
name|personId
init|=
operator|(
name|Holder
argument_list|<
name|String
argument_list|>
operator|)
name|msgList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Holder
argument_list|<
name|String
argument_list|>
name|ssn
init|=
operator|(
name|Holder
argument_list|<
name|String
argument_list|>
operator|)
name|msgList
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|Holder
argument_list|<
name|String
argument_list|>
name|name
init|=
operator|(
name|Holder
argument_list|<
name|String
argument_list|>
operator|)
name|msgList
operator|.
name|get
argument_list|(
literal|2
argument_list|)
decl_stmt|;
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
name|LOG
operator|.
name|info
argument_list|(
literal|"person id 123, so throwing exception"
argument_list|)
expr_stmt|;
comment|// Try to throw out the soap fault message
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
name|personFault
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
name|personFault
operator|.
name|setPersonId
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|wsdl_first
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
name|UnknownPersonFault
argument_list|(
literal|"Get the null value of person name"
argument_list|,
name|personFault
argument_list|)
decl_stmt|;
comment|// Since camel has its own exception handler framework, we can't throw the exception to trigger it
comment|// We just set the fault message in the exchange for camel-cxf component handling and return
name|exchange
operator|.
name|getFault
argument_list|()
operator|.
name|setBody
argument_list|(
name|fault
argument_list|)
expr_stmt|;
return|return;
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
literal|"123"
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"setting Bonjour as the response"
argument_list|)
expr_stmt|;
comment|// Set the response message, first element is the return value of the operation,
comment|// the others are the holders of method parameters
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|null
block|,
name|personId
block|,
name|ssn
block|,
name|name
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

begin_comment
comment|// END SNIPPET: personProcessor
end_comment

end_unit

