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
comment|/**  * An exception thrown if a value could not be converted to the required type  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|NoTypeConversionAvailableException
specifier|public
class|class
name|NoTypeConversionAvailableException
extends|extends
name|CamelException
block|{
DECL|field|value
specifier|private
specifier|final
specifier|transient
name|Object
name|value
decl_stmt|;
DECL|field|type
specifier|private
specifier|final
specifier|transient
name|Class
name|type
decl_stmt|;
DECL|method|NoTypeConversionAvailableException (Object value, Class type)
specifier|public
name|NoTypeConversionAvailableException
parameter_list|(
name|Object
name|value
parameter_list|,
name|Class
name|type
parameter_list|)
block|{
name|super
argument_list|(
name|createMessage
argument_list|(
name|value
argument_list|,
name|type
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
DECL|method|NoTypeConversionAvailableException (Object value, Class type, Throwable cause)
specifier|public
name|NoTypeConversionAvailableException
parameter_list|(
name|Object
name|value
parameter_list|,
name|Class
name|type
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|this
argument_list|(
name|value
argument_list|,
name|type
argument_list|)
expr_stmt|;
name|initCause
argument_list|(
name|cause
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the value which could not be converted      */
DECL|method|getValue ()
specifier|public
name|Object
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
comment|/**      * Returns the required<tt>to</tt> type      */
DECL|method|getToType ()
specifier|public
name|Class
name|getToType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
comment|/**      * Returns the required<tt>from</tt> type.      * Returns<tt>null</tt> if the provided value was null.      */
DECL|method|getFromType ()
specifier|public
name|Class
name|getFromType
parameter_list|()
block|{
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
return|return
name|value
operator|.
name|getClass
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
comment|/**      * Returns an error message for no type converter available.      */
DECL|method|createMessage (Object value, Class type)
specifier|public
specifier|static
name|String
name|createMessage
parameter_list|(
name|Object
name|value
parameter_list|,
name|Class
name|type
parameter_list|)
block|{
return|return
literal|"No type converter available to convert from type: "
operator|+
operator|(
name|value
operator|!=
literal|null
condition|?
name|value
operator|.
name|getClass
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
else|:
literal|null
operator|)
operator|+
literal|" to the required type: "
operator|+
name|type
operator|.
name|getCanonicalName
argument_list|()
operator|+
literal|" with value "
operator|+
name|value
return|;
block|}
block|}
end_class

end_unit

