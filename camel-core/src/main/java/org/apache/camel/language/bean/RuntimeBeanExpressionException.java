begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
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
name|RuntimeExpressionException
import|;
end_import

begin_comment
comment|/**  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|RuntimeBeanExpressionException
specifier|public
class|class
name|RuntimeBeanExpressionException
extends|extends
name|RuntimeExpressionException
block|{
DECL|field|exchange
specifier|private
name|Exchange
name|exchange
decl_stmt|;
DECL|field|bean
specifier|private
name|Object
name|bean
decl_stmt|;
DECL|field|method
specifier|private
name|String
name|method
decl_stmt|;
DECL|method|RuntimeBeanExpressionException (Exchange exchange, Object bean, String method, Throwable e)
specifier|public
name|RuntimeBeanExpressionException
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|bean
parameter_list|,
name|String
name|method
parameter_list|,
name|Throwable
name|e
parameter_list|)
block|{
name|super
argument_list|(
literal|"Failed to invoke method: "
operator|+
name|method
operator|+
literal|" on "
operator|+
name|bean
operator|+
literal|" due to: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
name|this
operator|.
name|bean
operator|=
name|bean
expr_stmt|;
name|this
operator|.
name|method
operator|=
name|method
expr_stmt|;
block|}
DECL|method|getBean ()
specifier|public
name|Object
name|getBean
parameter_list|()
block|{
return|return
name|bean
return|;
block|}
DECL|method|getExchange ()
specifier|public
name|Exchange
name|getExchange
parameter_list|()
block|{
return|return
name|exchange
return|;
block|}
DECL|method|getMethod ()
specifier|public
name|String
name|getMethod
parameter_list|()
block|{
return|return
name|method
return|;
block|}
block|}
end_class

end_unit

