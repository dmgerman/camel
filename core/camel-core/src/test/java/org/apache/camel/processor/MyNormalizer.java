begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
package|;
end_package

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
name|language
operator|.
name|XPath
import|;
end_import

begin_comment
comment|// START SNIPPET: example
end_comment

begin_class
DECL|class|MyNormalizer
specifier|public
class|class
name|MyNormalizer
block|{
DECL|method|employeeToPerson (Exchange exchange, @XPath(R) String name)
specifier|public
name|void
name|employeeToPerson
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
annotation|@
name|XPath
argument_list|(
literal|"/employee/name/text()"
argument_list|)
name|String
name|name
parameter_list|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|createPerson
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|customerToPerson (Exchange exchange, @XPath(R) String name)
specifier|public
name|void
name|customerToPerson
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
annotation|@
name|XPath
argument_list|(
literal|"/customer/@name"
argument_list|)
name|String
name|name
parameter_list|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|createPerson
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|createPerson (String name)
specifier|private
name|String
name|createPerson
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
literal|"<person name=\""
operator|+
name|name
operator|+
literal|"\"/>"
return|;
block|}
block|}
end_class

begin_comment
comment|// END SNIPPET: example
end_comment

end_unit

