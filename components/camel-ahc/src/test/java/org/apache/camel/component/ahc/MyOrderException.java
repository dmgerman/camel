begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ahc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ahc
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|MyOrderException
specifier|public
class|class
name|MyOrderException
extends|extends
name|Exception
implements|implements
name|Serializable
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|field|orderId
specifier|private
specifier|final
name|String
name|orderId
decl_stmt|;
DECL|method|MyOrderException (String orderId)
specifier|public
name|MyOrderException
parameter_list|(
name|String
name|orderId
parameter_list|)
block|{
name|super
argument_list|(
literal|"Unknown orderId: "
operator|+
name|orderId
argument_list|)
expr_stmt|;
name|this
operator|.
name|orderId
operator|=
name|orderId
expr_stmt|;
block|}
DECL|method|getOrderId ()
specifier|public
name|String
name|getOrderId
parameter_list|()
block|{
return|return
name|orderId
return|;
block|}
block|}
end_class

end_unit

