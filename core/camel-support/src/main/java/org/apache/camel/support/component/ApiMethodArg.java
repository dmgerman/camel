begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support.component
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|component
package|;
end_package

begin_class
DECL|class|ApiMethodArg
specifier|public
class|class
name|ApiMethodArg
block|{
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|type
specifier|private
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|type
decl_stmt|;
DECL|field|typeArgs
specifier|private
specifier|final
name|String
name|typeArgs
decl_stmt|;
DECL|method|ApiMethodArg (String name, Class<?> type, String typeArgs)
specifier|public
name|ApiMethodArg
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|String
name|typeArgs
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|typeArgs
operator|=
name|typeArgs
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|this
operator|.
name|name
return|;
block|}
DECL|method|getType ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getType
parameter_list|()
block|{
return|return
name|this
operator|.
name|type
return|;
block|}
DECL|method|getTypeArgs ()
specifier|public
name|String
name|getTypeArgs
parameter_list|()
block|{
return|return
name|this
operator|.
name|typeArgs
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|type
operator|.
name|getCanonicalName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|typeArgs
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|"<"
argument_list|)
operator|.
name|append
argument_list|(
name|typeArgs
argument_list|)
operator|.
name|append
argument_list|(
literal|">"
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
operator|.
name|append
argument_list|(
name|name
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|arg (String name, Class<?> type)
specifier|public
specifier|static
name|ApiMethodArg
name|arg
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
return|return
operator|new
name|ApiMethodArg
argument_list|(
name|name
argument_list|,
name|type
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|arg (String name, Class<?> type, String typeArgs)
specifier|public
specifier|static
name|ApiMethodArg
name|arg
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|String
name|typeArgs
parameter_list|)
block|{
return|return
operator|new
name|ApiMethodArg
argument_list|(
name|name
argument_list|,
name|type
argument_list|,
name|typeArgs
argument_list|)
return|;
block|}
block|}
end_class

end_unit

