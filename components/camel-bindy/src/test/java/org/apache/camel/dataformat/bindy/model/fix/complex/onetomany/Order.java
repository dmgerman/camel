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
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|OneToMany
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
literal|58
argument_list|)
comment|// Free text
DECL|field|text
specifier|private
name|String
name|text
decl_stmt|;
annotation|@
name|KeyValuePairField
argument_list|(
name|tag
operator|=
literal|777
argument_list|,
name|pattern
operator|=
literal|"dd-MM-yyyy HH:mm:ss"
argument_list|,
name|timezone
operator|=
literal|"GMT-3"
argument_list|)
comment|// created
DECL|field|created
specifier|private
name|Date
name|created
decl_stmt|;
annotation|@
name|OneToMany
argument_list|(
name|mappedTo
operator|=
literal|"org.apache.camel.dataformat.bindy.model.fix.complex.onetomany.Security"
argument_list|)
DECL|field|securities
specifier|private
name|List
argument_list|<
name|Security
argument_list|>
name|securities
decl_stmt|;
DECL|method|getSecurities ()
specifier|public
name|List
argument_list|<
name|Security
argument_list|>
name|getSecurities
parameter_list|()
block|{
return|return
name|securities
return|;
block|}
DECL|method|setSecurities (List<Security> securities)
specifier|public
name|void
name|setSecurities
parameter_list|(
name|List
argument_list|<
name|Security
argument_list|>
name|securities
parameter_list|)
block|{
name|this
operator|.
name|securities
operator|=
name|securities
expr_stmt|;
block|}
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
DECL|method|getCreated ()
specifier|public
name|Date
name|getCreated
parameter_list|()
block|{
return|return
name|created
return|;
block|}
DECL|method|setCreated (Date created)
specifier|public
name|void
name|setCreated
parameter_list|(
name|Date
name|created
parameter_list|)
block|{
name|this
operator|.
name|created
operator|=
name|created
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
name|StringBuilder
name|temp
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|temp
operator|.
name|append
argument_list|(
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
literal|", 58: "
operator|+
name|this
operator|.
name|text
operator|+
literal|", 777: "
operator|+
name|this
operator|.
name|created
argument_list|)
expr_stmt|;
name|temp
operator|.
name|append
argument_list|(
literal|"\r"
argument_list|)
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|securities
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Security
name|sec
range|:
name|this
operator|.
name|securities
control|)
block|{
name|temp
operator|.
name|append
argument_list|(
name|sec
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|temp
operator|.
name|append
argument_list|(
literal|"\r"
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|temp
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

