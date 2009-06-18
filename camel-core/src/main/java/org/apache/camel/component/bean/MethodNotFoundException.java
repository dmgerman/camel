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
name|CamelExchangeException
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

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|MethodNotFoundException
specifier|public
class|class
name|MethodNotFoundException
extends|extends
name|CamelExchangeException
block|{
DECL|field|methodName
specifier|private
specifier|final
name|String
name|methodName
decl_stmt|;
DECL|field|bean
specifier|private
specifier|final
name|Object
name|bean
decl_stmt|;
DECL|method|MethodNotFoundException (Exchange exchange, Object pojo, String methodName)
specifier|public
name|MethodNotFoundException
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|pojo
parameter_list|,
name|String
name|methodName
parameter_list|)
block|{
name|super
argument_list|(
literal|"Method with name: "
operator|+
name|methodName
operator|+
literal|" not found on bean: "
operator|+
name|pojo
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|this
operator|.
name|methodName
operator|=
name|methodName
expr_stmt|;
name|this
operator|.
name|bean
operator|=
name|pojo
expr_stmt|;
block|}
DECL|method|getMethodName ()
specifier|public
name|String
name|getMethodName
parameter_list|()
block|{
return|return
name|methodName
return|;
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
block|}
end_class

end_unit

