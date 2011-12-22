begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.bindy.model.fix.complex.onetomany
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
operator|.
name|model
operator|.
name|fix
operator|.
name|complex
operator|.
name|onetomany
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
name|dataformat
operator|.
name|bindy
operator|.
name|annotation
operator|.
name|KeyValuePairField
import|;
end_import

begin_comment
comment|// @Message(keyValuePairSeparator = "=", pairSeparator = "\\u0001", type = "FIX", version = "4.1")
end_comment

begin_class
DECL|class|Security
specifier|public
class|class
name|Security
block|{
annotation|@
name|KeyValuePairField
argument_list|(
name|tag
operator|=
literal|22
argument_list|)
comment|// Fund ID type (Sedol, ISIN, ...)
DECL|field|idSource
specifier|private
name|String
name|idSource
decl_stmt|;
annotation|@
name|KeyValuePairField
argument_list|(
name|tag
operator|=
literal|48
argument_list|)
comment|// Fund code
DECL|field|securityCode
specifier|private
name|String
name|securityCode
decl_stmt|;
annotation|@
name|KeyValuePairField
argument_list|(
name|tag
operator|=
literal|54
argument_list|)
comment|// Movement type ( 1 = Buy, 2 = sell)
DECL|field|side
specifier|private
name|String
name|side
decl_stmt|;
DECL|method|getIdSource ()
specifier|public
name|String
name|getIdSource
parameter_list|()
block|{
return|return
name|idSource
return|;
block|}
DECL|method|setIdSource (String source)
specifier|public
name|void
name|setIdSource
parameter_list|(
name|String
name|source
parameter_list|)
block|{
name|this
operator|.
name|idSource
operator|=
name|source
expr_stmt|;
block|}
DECL|method|getSecurityCode ()
specifier|public
name|String
name|getSecurityCode
parameter_list|()
block|{
return|return
name|securityCode
return|;
block|}
DECL|method|setSecurityCode (String securityCode)
specifier|public
name|void
name|setSecurityCode
parameter_list|(
name|String
name|securityCode
parameter_list|)
block|{
name|this
operator|.
name|securityCode
operator|=
name|securityCode
expr_stmt|;
block|}
DECL|method|getSide ()
specifier|public
name|String
name|getSide
parameter_list|()
block|{
return|return
name|side
return|;
block|}
DECL|method|setSide (String side)
specifier|public
name|void
name|setSide
parameter_list|(
name|String
name|side
parameter_list|)
block|{
name|this
operator|.
name|side
operator|=
name|side
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|Security
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|" --> 22: "
operator|+
name|this
operator|.
name|getIdSource
argument_list|()
operator|+
literal|", 48: "
operator|+
name|this
operator|.
name|getSecurityCode
argument_list|()
operator|+
literal|", 54: "
operator|+
name|this
operator|.
name|getSide
argument_list|()
return|;
block|}
block|}
end_class

end_unit

