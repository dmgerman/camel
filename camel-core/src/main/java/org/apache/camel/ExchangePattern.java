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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * Represents the kind of message exchange pattern  *  * @version $Revision$  */
end_comment

begin_enum
DECL|enum|ExchangePattern
specifier|public
enum|enum
name|ExchangePattern
block|{
DECL|enumConstant|InOnly
DECL|enumConstant|RobustInOnly
DECL|enumConstant|InOut
DECL|enumConstant|InOptionalOut
DECL|enumConstant|OutOnly
DECL|enumConstant|RobustOutOnly
DECL|enumConstant|OutIn
DECL|enumConstant|OutOptionalIn
name|InOnly
block|,
name|RobustInOnly
block|,
name|InOut
block|,
name|InOptionalOut
block|,
name|OutOnly
block|,
name|RobustOutOnly
block|,
name|OutIn
block|,
name|OutOptionalIn
block|;
DECL|field|MAP
specifier|protected
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|ExchangePattern
argument_list|>
name|MAP
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|ExchangePattern
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Returns the WSDL URI for this message exchange pattern      *      * @return the WSDL URI for this message exchange pattern      */
DECL|method|getWsdlUri ()
specifier|public
name|String
name|getWsdlUri
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
literal|"http://www.w3.org/ns/wsdl/in-only"
return|;
case|case
name|InOptionalOut
case|:
return|return
literal|"http://www.w3.org/ns/wsdl/in-optional-out"
return|;
case|case
name|InOut
case|:
return|return
literal|"http://www.w3.org/ns/wsdl/in-out"
return|;
case|case
name|OutIn
case|:
return|return
literal|"http://www.w3.org/ns/wsdl/out-in"
return|;
case|case
name|OutOnly
case|:
return|return
literal|"http://www.w3.org/ns/wsdl/out-only"
return|;
case|case
name|OutOptionalIn
case|:
return|return
literal|"http://www.w3.org/ns/wsdl/out-optional_in"
return|;
case|case
name|RobustInOnly
case|:
return|return
literal|"http://www.w3.org/ns/wsdl/robust-in-only"
return|;
case|case
name|RobustOutOnly
case|:
return|return
literal|"http://www.w3.org/ns/wsdl/robust-out-only"
return|;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unknown message exchange pattern: "
operator|+
name|this
argument_list|)
throw|;
block|}
block|}
comment|/**      * Return true if there can be an IN message      */
DECL|method|isInCapable ()
specifier|public
name|boolean
name|isInCapable
parameter_list|()
block|{
switch|switch
condition|(
name|this
condition|)
block|{
case|case
name|OutOnly
case|:
case|case
name|RobustOutOnly
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
case|case
name|RobustInOnly
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
case|case
name|OutOnly
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
comment|/**      * Converts the WSDL URI into a {@link ExchangePattern} instance      */
DECL|method|fromWsdlUri (String wsdlUri)
specifier|public
specifier|static
name|ExchangePattern
name|fromWsdlUri
parameter_list|(
name|String
name|wsdlUri
parameter_list|)
block|{
return|return
name|MAP
operator|.
name|get
argument_list|(
name|wsdlUri
argument_list|)
return|;
block|}
static|static
block|{
for|for
control|(
name|ExchangePattern
name|mep
range|:
name|values
argument_list|()
control|)
block|{
name|String
name|uri
init|=
name|mep
operator|.
name|getWsdlUri
argument_list|()
decl_stmt|;
name|MAP
operator|.
name|put
argument_list|(
name|uri
argument_list|,
name|mep
argument_list|)
expr_stmt|;
name|String
name|name
init|=
name|uri
operator|.
name|substring
argument_list|(
name|uri
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
operator|+
literal|1
argument_list|)
decl_stmt|;
name|MAP
operator|.
name|put
argument_list|(
literal|"http://www.w3.org/2004/08/wsdl/"
operator|+
name|name
argument_list|,
name|mep
argument_list|)
expr_stmt|;
name|MAP
operator|.
name|put
argument_list|(
literal|"http://www.w3.org/2006/01/wsdl/"
operator|+
name|name
argument_list|,
name|mep
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_enum

end_unit

