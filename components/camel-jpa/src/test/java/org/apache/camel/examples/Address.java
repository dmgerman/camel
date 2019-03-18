begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.examples
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|examples
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Entity
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|GeneratedValue
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Id
import|;
end_import

begin_class
annotation|@
name|Entity
DECL|class|Address
specifier|public
class|class
name|Address
block|{
annotation|@
name|Id
annotation|@
name|GeneratedValue
DECL|field|id
specifier|private
name|Long
name|id
decl_stmt|;
DECL|field|addressLine1
specifier|private
name|String
name|addressLine1
decl_stmt|;
DECL|field|addressLine2
specifier|private
name|String
name|addressLine2
decl_stmt|;
DECL|method|getId ()
specifier|public
name|Long
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
DECL|method|setId (Long id)
specifier|public
name|void
name|setId
parameter_list|(
name|Long
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
DECL|method|getAddressLine1 ()
specifier|public
name|String
name|getAddressLine1
parameter_list|()
block|{
return|return
name|addressLine1
return|;
block|}
DECL|method|setAddressLine1 (String addressLine1)
specifier|public
name|void
name|setAddressLine1
parameter_list|(
name|String
name|addressLine1
parameter_list|)
block|{
name|this
operator|.
name|addressLine1
operator|=
name|addressLine1
expr_stmt|;
block|}
DECL|method|getAddressLine2 ()
specifier|public
name|String
name|getAddressLine2
parameter_list|()
block|{
return|return
name|addressLine2
return|;
block|}
DECL|method|setAddressLine2 (String addressLine2)
specifier|public
name|void
name|setAddressLine2
parameter_list|(
name|String
name|addressLine2
parameter_list|)
block|{
name|this
operator|.
name|addressLine2
operator|=
name|addressLine2
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
comment|// OpenJPA warns about fields being accessed directly in methods if NOT using the corresponding getters.
return|return
literal|"Address[id: "
operator|+
name|getId
argument_list|()
operator|+
literal|", addressLine1: "
operator|+
name|getAddressLine1
argument_list|()
operator|+
literal|", addressLine2: "
operator|+
name|getAddressLine2
argument_list|()
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

