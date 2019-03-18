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

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlEnum
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlType
import|;
end_import

begin_comment
comment|/**  * Represents the kind of message exchange pattern  */
end_comment

begin_enum
annotation|@
name|XmlType
annotation|@
name|XmlEnum
DECL|enum|ExchangePattern
specifier|public
enum|enum
name|ExchangePattern
block|{
DECL|enumConstant|InOnly
DECL|enumConstant|InOut
DECL|enumConstant|InOptionalOut
name|InOnly
block|,
name|InOut
block|,
name|InOptionalOut
block|;
comment|/**      * Return true if there can be an IN message      */
DECL|method|isInCapable ()
specifier|public
name|boolean
name|isInCapable
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
comment|/**      * Return true if there can be an OUT message      */
DECL|method|isOutCapable ()
specifier|public
name|boolean
name|isOutCapable
parameter_list|()
block|{
switch|switch
condition|(
name|this
condition|)
block|{
case|case
name|InOnly
case|:
return|return
literal|false
return|;
default|default:
return|return
literal|true
return|;
block|}
block|}
comment|/**      * Return true if there can be a FAULT message      */
DECL|method|isFaultCapable ()
specifier|public
name|boolean
name|isFaultCapable
parameter_list|()
block|{
switch|switch
condition|(
name|this
condition|)
block|{
case|case
name|InOnly
case|:
return|return
literal|false
return|;
default|default:
return|return
literal|true
return|;
block|}
block|}
DECL|method|asEnum (String value)
specifier|public
specifier|static
name|ExchangePattern
name|asEnum
parameter_list|(
name|String
name|value
parameter_list|)
block|{
try|try
block|{
return|return
name|valueOf
argument_list|(
name|value
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unknown message exchange pattern: "
operator|+
name|value
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_enum

end_unit

