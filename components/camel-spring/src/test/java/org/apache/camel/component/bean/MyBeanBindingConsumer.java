begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
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
name|Header
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
name|MessageDriven
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
name|ProducerTemplate
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
name|Bean
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
name|Constant
import|;
end_import

begin_comment
comment|/**  * Consumer using bean binding with an injected expressions such as: @Bean, @Constant etc.  */
end_comment

begin_class
DECL|class|MyBeanBindingConsumer
specifier|public
class|class
name|MyBeanBindingConsumer
block|{
DECL|field|template
specifier|private
name|ProducerTemplate
name|template
decl_stmt|;
annotation|@
name|MessageDriven
argument_list|(
name|uri
operator|=
literal|"direct:startBeanExpression"
argument_list|)
DECL|method|doSomethingBeanExpression (String payload, @Bean(value = R) int count)
specifier|public
name|void
name|doSomethingBeanExpression
parameter_list|(
name|String
name|payload
parameter_list|,
annotation|@
name|Bean
argument_list|(
name|value
operator|=
literal|"myCounter"
argument_list|)
name|int
name|count
parameter_list|)
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"mock:result"
argument_list|,
literal|"Bye "
operator|+
name|payload
argument_list|,
literal|"count"
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
annotation|@
name|MessageDriven
argument_list|(
name|uri
operator|=
literal|"direct:startConstantExpression"
argument_list|)
DECL|method|doSomethingConstantExpression (String payload, @Constant(value = R) int count)
specifier|public
name|void
name|doSomethingConstantExpression
parameter_list|(
name|String
name|payload
parameter_list|,
annotation|@
name|Constant
argument_list|(
name|value
operator|=
literal|"5"
argument_list|)
name|int
name|count
parameter_list|)
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"mock:result"
argument_list|,
literal|"Bye "
operator|+
name|payload
argument_list|,
literal|"count"
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
annotation|@
name|MessageDriven
argument_list|(
name|uri
operator|=
literal|"direct:startHeaderExpression"
argument_list|)
DECL|method|doSomethingHeaderExpression (String payload, @Header(name = R) int count)
specifier|public
name|void
name|doSomethingHeaderExpression
parameter_list|(
name|String
name|payload
parameter_list|,
annotation|@
name|Header
argument_list|(
name|name
operator|=
literal|"number"
argument_list|)
name|int
name|count
parameter_list|)
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"mock:result"
argument_list|,
literal|"Bye "
operator|+
name|payload
argument_list|,
literal|"count"
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
annotation|@
name|MessageDriven
argument_list|(
name|uri
operator|=
literal|"direct:startMany"
argument_list|)
DECL|method|doSomethingManyExpression (String payload, @Constant(value = R) int count, @Header(name = R) int number)
specifier|public
name|void
name|doSomethingManyExpression
parameter_list|(
name|String
name|payload
parameter_list|,
annotation|@
name|Constant
argument_list|(
name|value
operator|=
literal|"5"
argument_list|)
name|int
name|count
parameter_list|,
annotation|@
name|Header
argument_list|(
name|name
operator|=
literal|"number"
argument_list|)
name|int
name|number
parameter_list|)
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"mock:result"
argument_list|,
literal|"Bye "
operator|+
name|payload
argument_list|,
literal|"count"
argument_list|,
name|count
operator|*
name|number
argument_list|)
expr_stmt|;
block|}
DECL|method|setTemplate (ProducerTemplate template)
specifier|public
name|void
name|setTemplate
parameter_list|(
name|ProducerTemplate
name|template
parameter_list|)
block|{
name|this
operator|.
name|template
operator|=
name|template
expr_stmt|;
block|}
block|}
end_class

end_unit

