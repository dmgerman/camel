begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|Exchange
import|;
end_import

begin_comment
comment|/**  * A base class useful for implementing other typesafe exchanges  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|ExchangeSupport
specifier|public
specifier|abstract
class|class
name|ExchangeSupport
parameter_list|<
name|M
parameter_list|,
name|R
parameter_list|,
name|F
parameter_list|>
implements|implements
name|Exchange
argument_list|<
name|M
argument_list|,
name|R
argument_list|,
name|F
argument_list|>
block|{
DECL|field|request
specifier|private
name|M
name|request
decl_stmt|;
DECL|field|response
specifier|private
name|R
name|response
decl_stmt|;
DECL|field|fault
specifier|private
name|F
name|fault
decl_stmt|;
DECL|field|exception
specifier|private
name|Exception
name|exception
decl_stmt|;
DECL|method|getException ()
specifier|public
name|Exception
name|getException
parameter_list|()
block|{
return|return
name|exception
return|;
block|}
DECL|method|setException (Exception exception)
specifier|public
name|void
name|setException
parameter_list|(
name|Exception
name|exception
parameter_list|)
block|{
name|this
operator|.
name|exception
operator|=
name|exception
expr_stmt|;
block|}
DECL|method|getFault ()
specifier|public
name|F
name|getFault
parameter_list|()
block|{
return|return
name|fault
return|;
block|}
DECL|method|setFault (F fault)
specifier|public
name|void
name|setFault
parameter_list|(
name|F
name|fault
parameter_list|)
block|{
name|this
operator|.
name|fault
operator|=
name|fault
expr_stmt|;
block|}
DECL|method|getRequest ()
specifier|public
name|M
name|getRequest
parameter_list|()
block|{
return|return
name|request
return|;
block|}
DECL|method|setRequest (M request)
specifier|public
name|void
name|setRequest
parameter_list|(
name|M
name|request
parameter_list|)
block|{
name|this
operator|.
name|request
operator|=
name|request
expr_stmt|;
block|}
DECL|method|getResponse ()
specifier|public
name|R
name|getResponse
parameter_list|()
block|{
return|return
name|response
return|;
block|}
DECL|method|setResponse (R response)
specifier|public
name|void
name|setResponse
parameter_list|(
name|R
name|response
parameter_list|)
block|{
name|this
operator|.
name|response
operator|=
name|response
expr_stmt|;
block|}
block|}
end_class

end_unit

