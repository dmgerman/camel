begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.xml
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|xml
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathException
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
name|RuntimeExpressionException
import|;
end_import

begin_comment
comment|/**  * An exception thrown if am XPath expression could not be parsed or evaluated  */
end_comment

begin_class
DECL|class|InvalidXPathExpression
specifier|public
class|class
name|InvalidXPathExpression
extends|extends
name|RuntimeExpressionException
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|9171451033826915273L
decl_stmt|;
DECL|field|xpath
specifier|private
specifier|final
name|String
name|xpath
decl_stmt|;
DECL|method|InvalidXPathExpression (String xpath, XPathException e)
specifier|public
name|InvalidXPathExpression
parameter_list|(
name|String
name|xpath
parameter_list|,
name|XPathException
name|e
parameter_list|)
block|{
name|super
argument_list|(
literal|"Invalid xpath: "
operator|+
name|xpath
operator|+
literal|". Reason: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|this
operator|.
name|xpath
operator|=
name|xpath
expr_stmt|;
block|}
DECL|method|getXpath ()
specifier|public
name|String
name|getXpath
parameter_list|()
block|{
return|return
name|xpath
return|;
block|}
block|}
end_class

end_unit

