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
comment|/**  * @deprecated use {@link InvalidTypeException}  * @version $Revision$  */
end_comment

begin_class
DECL|class|InvalidHeaderTypeException
specifier|public
class|class
name|InvalidHeaderTypeException
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
literal|8417806626073055262L
decl_stmt|;
DECL|field|headerValue
specifier|private
specifier|final
name|Object
name|headerValue
decl_stmt|;
DECL|method|InvalidHeaderTypeException (Throwable cause, Object headerValue)
specifier|public
name|InvalidHeaderTypeException
parameter_list|(
name|Throwable
name|cause
parameter_list|,
name|Object
name|headerValue
parameter_list|)
block|{
name|super
argument_list|(
name|cause
operator|.
name|getMessage
argument_list|()
operator|+
literal|" headerValue is: "
operator|+
name|headerValue
operator|+
literal|" of type: "
operator|+
name|typeName
argument_list|(
name|headerValue
argument_list|)
argument_list|,
name|cause
argument_list|)
expr_stmt|;
name|this
operator|.
name|headerValue
operator|=
name|headerValue
expr_stmt|;
block|}
DECL|method|InvalidHeaderTypeException (String message, Object headerValue)
specifier|public
name|InvalidHeaderTypeException
parameter_list|(
name|String
name|message
parameter_list|,
name|Object
name|headerValue
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|this
operator|.
name|headerValue
operator|=
name|headerValue
expr_stmt|;
block|}
comment|/**      * Returns the actual header value      */
DECL|method|getHeaderValue ()
specifier|public
name|Object
name|getHeaderValue
parameter_list|()
block|{
return|return
name|headerValue
return|;
block|}
DECL|method|typeName (Object headerValue)
specifier|protected
specifier|static
name|String
name|typeName
parameter_list|(
name|Object
name|headerValue
parameter_list|)
block|{
return|return
operator|(
name|headerValue
operator|!=
literal|null
operator|)
condition|?
name|headerValue
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
else|:
literal|"null"
return|;
block|}
block|}
end_class

end_unit

