begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cdi.internal
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cdi
operator|.
name|internal
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|Bean
import|;
end_import

begin_comment
comment|/**  * Contains the bean and the consume methods  */
end_comment

begin_class
DECL|class|BeanAdapter
specifier|public
class|class
name|BeanAdapter
block|{
DECL|field|bean
specifier|private
specifier|final
name|Bean
argument_list|<
name|?
argument_list|>
name|bean
decl_stmt|;
DECL|field|consumeMethods
specifier|private
specifier|final
name|List
argument_list|<
name|Method
argument_list|>
name|consumeMethods
init|=
operator|new
name|ArrayList
argument_list|<
name|Method
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|BeanAdapter (Bean<?> bean)
specifier|public
name|BeanAdapter
parameter_list|(
name|Bean
argument_list|<
name|?
argument_list|>
name|bean
parameter_list|)
block|{
name|this
operator|.
name|bean
operator|=
name|bean
expr_stmt|;
block|}
DECL|method|getBean ()
specifier|public
name|Bean
argument_list|<
name|?
argument_list|>
name|getBean
parameter_list|()
block|{
return|return
name|bean
return|;
block|}
DECL|method|getConsumeMethods ()
specifier|public
name|List
argument_list|<
name|Method
argument_list|>
name|getConsumeMethods
parameter_list|()
block|{
return|return
name|consumeMethods
return|;
block|}
DECL|method|addConsumeMethod (Method method)
specifier|public
name|void
name|addConsumeMethod
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
name|consumeMethods
operator|.
name|add
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

