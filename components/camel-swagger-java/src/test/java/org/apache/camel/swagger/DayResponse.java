begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.swagger
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|swagger
package|;
end_package

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|annotations
operator|.
name|ApiModel
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|annotations
operator|.
name|ApiModelProperty
import|;
end_import

begin_class
annotation|@
name|ApiModel
argument_list|(
name|description
operator|=
literal|"Represents a day"
argument_list|)
DECL|class|DayResponse
specifier|public
class|class
name|DayResponse
block|{
DECL|field|day
specifier|private
name|String
name|day
decl_stmt|;
annotation|@
name|ApiModelProperty
argument_list|(
name|value
operator|=
literal|"The day"
argument_list|,
name|required
operator|=
literal|true
argument_list|)
DECL|method|getDay ()
specifier|public
name|String
name|getDay
parameter_list|()
block|{
return|return
name|day
return|;
block|}
DECL|method|setDay (String day)
specifier|public
name|void
name|setDay
parameter_list|(
name|String
name|day
parameter_list|)
block|{
name|this
operator|.
name|day
operator|=
name|day
expr_stmt|;
block|}
block|}
end_class

end_unit

