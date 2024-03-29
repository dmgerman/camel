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
comment|/**  * An exception caused when an invalid property name is used on an object  */
end_comment

begin_class
DECL|class|InvalidPropertyException
specifier|public
class|class
name|InvalidPropertyException
extends|extends
name|RuntimeCamelException
block|{
DECL|field|owner
specifier|private
specifier|final
specifier|transient
name|Object
name|owner
decl_stmt|;
DECL|field|propertyName
specifier|private
specifier|final
name|String
name|propertyName
decl_stmt|;
DECL|method|InvalidPropertyException (Object owner, String propertyName)
specifier|public
name|InvalidPropertyException
parameter_list|(
name|Object
name|owner
parameter_list|,
name|String
name|propertyName
parameter_list|)
block|{
name|this
argument_list|(
name|owner
argument_list|,
name|propertyName
argument_list|,
name|owner
operator|!=
literal|null
condition|?
name|owner
operator|.
name|getClass
argument_list|()
else|:
name|Object
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|InvalidPropertyException (Object owner, String propertyName, Class<?> type)
specifier|public
name|InvalidPropertyException
parameter_list|(
name|Object
name|owner
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|super
argument_list|(
literal|"No '"
operator|+
name|propertyName
operator|+
literal|"' property available on type: "
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
literal|" in: "
operator|+
name|owner
argument_list|)
expr_stmt|;
name|this
operator|.
name|owner
operator|=
name|owner
expr_stmt|;
name|this
operator|.
name|propertyName
operator|=
name|propertyName
expr_stmt|;
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
DECL|method|getOwner ()
specifier|public
name|Object
name|getOwner
parameter_list|()
block|{
return|return
name|owner
return|;
block|}
block|}
end_class

end_unit

