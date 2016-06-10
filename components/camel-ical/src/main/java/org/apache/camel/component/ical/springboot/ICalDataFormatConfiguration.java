begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ical.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ical
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_comment
comment|/**  * Camel iCal component  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.dataformat.ical"
argument_list|)
DECL|class|ICalDataFormatConfiguration
specifier|public
class|class
name|ICalDataFormatConfiguration
block|{
comment|/**      * Whether to validate.      */
DECL|field|validating
specifier|private
name|Boolean
name|validating
decl_stmt|;
DECL|method|getValidating ()
specifier|public
name|Boolean
name|getValidating
parameter_list|()
block|{
return|return
name|validating
return|;
block|}
DECL|method|setValidating (Boolean validating)
specifier|public
name|void
name|setValidating
parameter_list|(
name|Boolean
name|validating
parameter_list|)
block|{
name|this
operator|.
name|validating
operator|=
name|validating
expr_stmt|;
block|}
block|}
end_class

end_unit

