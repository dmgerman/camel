begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.apns.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|apns
operator|.
name|model
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_class
DECL|class|InactiveDevice
specifier|public
class|class
name|InactiveDevice
implements|implements
name|Serializable
block|{
DECL|field|deviceToken
specifier|private
name|String
name|deviceToken
decl_stmt|;
DECL|field|date
specifier|private
name|Date
name|date
decl_stmt|;
DECL|method|InactiveDevice (String deviceToken, Date date)
specifier|public
name|InactiveDevice
parameter_list|(
name|String
name|deviceToken
parameter_list|,
name|Date
name|date
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|deviceToken
operator|=
name|deviceToken
expr_stmt|;
name|this
operator|.
name|date
operator|=
name|date
expr_stmt|;
block|}
DECL|method|getDeviceToken ()
specifier|public
name|String
name|getDeviceToken
parameter_list|()
block|{
return|return
name|deviceToken
return|;
block|}
DECL|method|setDeviceToken (String deviceToken)
specifier|public
name|void
name|setDeviceToken
parameter_list|(
name|String
name|deviceToken
parameter_list|)
block|{
name|this
operator|.
name|deviceToken
operator|=
name|deviceToken
expr_stmt|;
block|}
DECL|method|getDate ()
specifier|public
name|Date
name|getDate
parameter_list|()
block|{
return|return
name|date
return|;
block|}
DECL|method|setDate (Date date)
specifier|public
name|void
name|setDate
parameter_list|(
name|Date
name|date
parameter_list|)
block|{
name|this
operator|.
name|date
operator|=
name|date
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
literal|"InactiveDevice["
operator|+
name|deviceToken
operator|+
literal|", "
operator|+
name|date
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

