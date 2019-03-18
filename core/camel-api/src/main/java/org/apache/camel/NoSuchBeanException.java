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
comment|/**  * A runtime exception if a given bean could not be found in the {@link org.apache.camel.spi.Registry}  */
end_comment

begin_class
DECL|class|NoSuchBeanException
specifier|public
class|class
name|NoSuchBeanException
extends|extends
name|RuntimeCamelException
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|8721487431101572630L
decl_stmt|;
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|method|NoSuchBeanException (String name)
specifier|public
name|NoSuchBeanException
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|super
argument_list|(
literal|"No bean could be found in the registry for: "
operator|+
name|name
argument_list|)
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|NoSuchBeanException (String name, String type)
specifier|public
name|NoSuchBeanException
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|type
parameter_list|)
block|{
name|super
argument_list|(
literal|"No bean could be found in the registry"
operator|+
operator|(
name|name
operator|!=
literal|null
condition|?
literal|" for: "
operator|+
name|name
else|:
literal|""
operator|)
operator|+
literal|" of type: "
operator|+
name|type
argument_list|)
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|NoSuchBeanException (String name, Throwable cause)
specifier|public
name|NoSuchBeanException
parameter_list|(
name|String
name|name
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
literal|"No bean could be found in the registry for: "
operator|+
name|name
operator|+
literal|". Cause: "
operator|+
name|cause
operator|.
name|getMessage
argument_list|()
argument_list|,
name|cause
argument_list|)
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|NoSuchBeanException (String name, String message, Throwable cause)
specifier|public
name|NoSuchBeanException
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|message
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|cause
argument_list|)
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
block|}
end_class

end_unit

