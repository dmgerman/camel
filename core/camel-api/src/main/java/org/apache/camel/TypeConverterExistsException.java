begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * Exception when failing to add type converters due there is already an existing type converter.  */
end_comment

begin_class
DECL|class|TypeConverterExistsException
specifier|public
class|class
name|TypeConverterExistsException
extends|extends
name|RuntimeCamelException
block|{
DECL|field|toType
specifier|private
specifier|final
specifier|transient
name|Class
argument_list|<
name|?
argument_list|>
name|toType
decl_stmt|;
DECL|field|fromType
specifier|private
specifier|final
specifier|transient
name|Class
argument_list|<
name|?
argument_list|>
name|fromType
decl_stmt|;
DECL|method|TypeConverterExistsException (Class<?> toType, Class<?> fromType)
specifier|public
name|TypeConverterExistsException
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|toType
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|fromType
parameter_list|)
block|{
name|super
argument_list|(
literal|"Failed to add type converter because a type converter exists. "
operator|+
name|fromType
operator|+
literal|" -> "
operator|+
name|toType
argument_list|)
expr_stmt|;
name|this
operator|.
name|toType
operator|=
name|toType
expr_stmt|;
name|this
operator|.
name|fromType
operator|=
name|fromType
expr_stmt|;
block|}
DECL|method|getToType ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getToType
parameter_list|()
block|{
return|return
name|toType
return|;
block|}
DECL|method|getFromType ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getFromType
parameter_list|()
block|{
return|return
name|fromType
return|;
block|}
block|}
end_class

end_unit

