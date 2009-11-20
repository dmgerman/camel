begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.bindy.model.fix.simple
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
name|simple
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
name|Link
import|;
end_import

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
name|Message
import|;
end_import

begin_class
annotation|@
name|Message
argument_list|(
name|keyValuePairSeparator
operator|=
literal|"="
argument_list|,
name|pairSeparator
operator|=
literal|"\\u0001"
argument_list|,
name|type
operator|=
literal|"FIX"
argument_list|,
name|version
operator|=
literal|"4.1"
argument_list|)
DECL|class|Order
specifier|public
class|class
name|Order
block|{
annotation|@
name|Link
DECL|field|header
name|Header
name|header
decl_stmt|;
annotation|@
name|Link
DECL|field|trailer
name|Trailer
name|trailer
decl_stmt|;
annotation|@
name|KeyValuePairField
argument_list|(
name|tag
operator|=
literal|1
argument_list|)
comment|// Client reference
DECL|field|account
specifier|private
name|String
name|account
decl_stmt|;
annotation|@
name|KeyValuePairField
argument_list|(
name|tag
operator|=
literal|11
argument_list|)
comment|// Order reference
DECL|field|clOrdId
specifier|private
name|String
name|clOrdId
decl_stmt|;
annotation|@
name|KeyValuePairField
argument_list|(
name|tag
operator|=
literal|22
argument_list|)
comment|// Fund ID type (Sedol, ISIN, ...)
DECL|field|iDSource
specifier|private
name|String
name|iDSource
decl_stmt|;
annotation|@
name|KeyValuePairField
argument_list|(
name|tag
operator|=
literal|48
argument_list|)
comment|// Fund code
DECL|field|securityId
specifier|private
name|String
name|securityId
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
annotation|@
name|KeyValuePairField
argument_list|(
name|tag
operator|=
literal|58
argument_list|)
comment|// Free text
DECL|field|text
specifier|private
name|String
name|text
decl_stmt|;
DECL|method|getHeader ()
specifier|public
name|Header
name|getHeader
parameter_list|()
block|{
return|return
name|header
return|;
block|}
DECL|method|setHeader (Header header)
specifier|public
name|void
name|setHeader
parameter_list|(
name|Header
name|header
parameter_list|)
block|{
name|this
operator|.
name|header
operator|=
name|header
expr_stmt|;
block|}
DECL|method|getTrailer ()
specifier|public
name|Trailer
name|getTrailer
parameter_list|()
block|{
return|return
name|trailer
return|;
block|}
DECL|method|setTrailer (Trailer trailer)
specifier|public
name|void
name|setTrailer
parameter_list|(
name|Trailer
name|trailer
parameter_list|)
block|{
name|this
operator|.
name|trailer
operator|=
name|trailer
expr_stmt|;
block|}
DECL|method|getAccount ()
specifier|public
name|String
name|getAccount
parameter_list|()
block|{
return|return
name|account
return|;
block|}
DECL|method|setAccount (String account)
specifier|public
name|void
name|setAccount
parameter_list|(
name|String
name|account
parameter_list|)
block|{
name|this
operator|.
name|account
operator|=
name|account
expr_stmt|;
block|}
DECL|method|getClOrdId ()
specifier|public
name|String
name|getClOrdId
parameter_list|()
block|{
return|return
name|clOrdId
return|;
block|}
DECL|method|setClOrdId (String clOrdId)
specifier|public
name|void
name|setClOrdId
parameter_list|(
name|String
name|clOrdId
parameter_list|)
block|{
name|this
operator|.
name|clOrdId
operator|=
name|clOrdId
expr_stmt|;
block|}
DECL|method|getIDSource ()
specifier|public
name|String
name|getIDSource
parameter_list|()
block|{
return|return
name|iDSource
return|;
block|}
DECL|method|setIDSource (String source)
specifier|public
name|void
name|setIDSource
parameter_list|(
name|String
name|source
parameter_list|)
block|{
name|this
operator|.
name|iDSource
operator|=
name|source
expr_stmt|;
block|}
DECL|method|getSecurityId ()
specifier|public
name|String
name|getSecurityId
parameter_list|()
block|{
return|return
name|securityId
return|;
block|}
DECL|method|setSecurityId (String securityId)
specifier|public
name|void
name|setSecurityId
parameter_list|(
name|String
name|securityId
parameter_list|)
block|{
name|this
operator|.
name|securityId
operator|=
name|securityId
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
DECL|method|getText ()
specifier|public
name|String
name|getText
parameter_list|()
block|{
return|return
name|this
operator|.
name|text
return|;
block|}
DECL|method|setText (String text)
specifier|public
name|void
name|setText
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|this
operator|.
name|text
operator|=
name|text
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
name|Order
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|" --> 1: "
operator|+
name|this
operator|.
name|account
operator|+
literal|", 11: "
operator|+
name|this
operator|.
name|clOrdId
operator|+
literal|", 22: "
operator|+
name|this
operator|.
name|iDSource
operator|+
literal|", 48: "
operator|+
name|this
operator|.
name|securityId
operator|+
literal|", 54: "
operator|+
name|this
operator|.
name|side
operator|+
literal|", 58: "
operator|+
name|this
operator|.
name|text
return|;
block|}
block|}
end_class

end_unit

