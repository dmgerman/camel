begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.loanbroker.webservice.version.bank
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|loanbroker
operator|.
name|webservice
operator|.
name|version
operator|.
name|bank
package|;
end_package

begin_class
DECL|class|BankQuote
specifier|public
class|class
name|BankQuote
block|{
DECL|field|bankName
specifier|private
name|String
name|bankName
decl_stmt|;
DECL|field|ssn
specifier|private
name|String
name|ssn
decl_stmt|;
DECL|field|rate
specifier|private
name|Double
name|rate
decl_stmt|;
DECL|method|BankQuote ()
specifier|public
name|BankQuote
parameter_list|()
block|{     }
DECL|method|BankQuote (String name, String s, Double r)
specifier|public
name|BankQuote
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|s
parameter_list|,
name|Double
name|r
parameter_list|)
block|{
name|bankName
operator|=
name|name
expr_stmt|;
name|ssn
operator|=
name|s
expr_stmt|;
name|rate
operator|=
name|r
expr_stmt|;
block|}
DECL|method|setBankName (String name)
specifier|public
name|void
name|setBankName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|bankName
operator|=
name|name
expr_stmt|;
block|}
DECL|method|setSsn (String s)
specifier|public
name|void
name|setSsn
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|ssn
operator|=
name|s
expr_stmt|;
block|}
DECL|method|setRate (Double r)
specifier|public
name|void
name|setRate
parameter_list|(
name|Double
name|r
parameter_list|)
block|{
name|rate
operator|=
name|r
expr_stmt|;
block|}
DECL|method|getBankName ()
specifier|public
name|String
name|getBankName
parameter_list|()
block|{
return|return
name|bankName
return|;
block|}
DECL|method|getSsn ()
specifier|public
name|String
name|getSsn
parameter_list|()
block|{
return|return
name|ssn
return|;
block|}
DECL|method|getRate ()
specifier|public
name|Double
name|getRate
parameter_list|()
block|{
return|return
name|rate
return|;
block|}
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"[ ssn:"
operator|+
name|ssn
operator|+
literal|" bank:"
operator|+
name|bankName
operator|+
literal|" rate:"
operator|+
name|rate
operator|+
literal|" ]"
return|;
block|}
block|}
end_class

end_unit

