begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.bindy.model.complex.twoclassesandonelink
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
name|complex
operator|.
name|twoclassesandonelink
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
name|DataField
import|;
end_import

begin_class
DECL|class|Client
specifier|public
class|class
name|Client
block|{
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|1
argument_list|)
DECL|field|clientNr
specifier|private
name|String
name|clientNr
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|2
argument_list|)
DECL|field|firstName
specifier|private
name|String
name|firstName
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|3
argument_list|)
DECL|field|lastName
specifier|private
name|String
name|lastName
decl_stmt|;
DECL|method|getClientNr ()
specifier|public
name|String
name|getClientNr
parameter_list|()
block|{
return|return
name|clientNr
return|;
block|}
DECL|method|setClientNr (String clientNr)
specifier|public
name|void
name|setClientNr
parameter_list|(
name|String
name|clientNr
parameter_list|)
block|{
name|this
operator|.
name|clientNr
operator|=
name|clientNr
expr_stmt|;
block|}
DECL|method|getFirstName ()
specifier|public
name|String
name|getFirstName
parameter_list|()
block|{
return|return
name|firstName
return|;
block|}
DECL|method|setFirstName (String firstName)
specifier|public
name|void
name|setFirstName
parameter_list|(
name|String
name|firstName
parameter_list|)
block|{
name|this
operator|.
name|firstName
operator|=
name|firstName
expr_stmt|;
block|}
DECL|method|getLastName ()
specifier|public
name|String
name|getLastName
parameter_list|()
block|{
return|return
name|lastName
return|;
block|}
DECL|method|setLastName (String lastName)
specifier|public
name|void
name|setLastName
parameter_list|(
name|String
name|lastName
parameter_list|)
block|{
name|this
operator|.
name|lastName
operator|=
name|lastName
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
literal|"Model : "
operator|+
name|Client
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|" : "
operator|+
name|this
operator|.
name|getClientNr
argument_list|()
operator|+
literal|", "
operator|+
name|this
operator|.
name|getFirstName
argument_list|()
operator|+
literal|", "
operator|+
name|this
operator|.
name|getLastName
argument_list|()
return|;
block|}
block|}
end_class

end_unit

