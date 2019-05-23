begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

begin_comment
comment|/**  * Error binding property to a bean.  */
end_comment

begin_class
DECL|class|PropertyBindingException
specifier|public
class|class
name|PropertyBindingException
extends|extends
name|RuntimeCamelException
block|{
DECL|field|target
specifier|private
specifier|final
name|Object
name|target
decl_stmt|;
DECL|field|propertyName
specifier|private
specifier|final
name|String
name|propertyName
decl_stmt|;
DECL|method|PropertyBindingException (Object target, String propertyName)
specifier|public
name|PropertyBindingException
parameter_list|(
name|Object
name|target
parameter_list|,
name|String
name|propertyName
parameter_list|)
block|{
name|super
argument_list|(
literal|"No such property: "
operator|+
name|propertyName
operator|+
literal|" on bean: "
operator|+
name|target
argument_list|)
expr_stmt|;
name|this
operator|.
name|target
operator|=
name|target
expr_stmt|;
name|this
operator|.
name|propertyName
operator|=
name|propertyName
expr_stmt|;
block|}
DECL|method|PropertyBindingException (Object target, String propertyName, Exception e)
specifier|public
name|PropertyBindingException
parameter_list|(
name|Object
name|target
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|Exception
name|e
parameter_list|)
block|{
name|super
argument_list|(
literal|"Error binding property: "
operator|+
name|propertyName
operator|+
literal|" on bean: "
operator|+
name|target
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|this
operator|.
name|target
operator|=
name|target
expr_stmt|;
name|this
operator|.
name|propertyName
operator|=
name|propertyName
expr_stmt|;
block|}
DECL|method|PropertyBindingException (Object target, Exception e)
specifier|public
name|PropertyBindingException
parameter_list|(
name|Object
name|target
parameter_list|,
name|Exception
name|e
parameter_list|)
block|{
name|super
argument_list|(
literal|"Error binding properties on bean: "
operator|+
name|target
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|this
operator|.
name|target
operator|=
name|target
expr_stmt|;
name|this
operator|.
name|propertyName
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|getTarget ()
specifier|public
name|Object
name|getTarget
parameter_list|()
block|{
return|return
name|target
return|;
block|}
DECL|method|getPropertyName ()
specifier|public
name|String
name|getPropertyName
parameter_list|()
block|{
return|return
name|propertyName
return|;
block|}
block|}
end_class

end_unit

