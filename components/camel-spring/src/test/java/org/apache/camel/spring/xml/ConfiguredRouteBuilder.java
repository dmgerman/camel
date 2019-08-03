begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.xml
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|xml
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
name|builder
operator|.
name|RouteBuilder
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_class
DECL|class|ConfiguredRouteBuilder
specifier|public
class|class
name|ConfiguredRouteBuilder
extends|extends
name|RouteBuilder
block|{
DECL|field|fromUri
specifier|private
name|String
name|fromUri
decl_stmt|;
DECL|field|toUri
specifier|private
name|String
name|toUri
decl_stmt|;
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|fromUri
argument_list|,
literal|"fromUri"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|toUri
argument_list|,
literal|"toUri"
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|fromUri
argument_list|)
operator|.
name|to
argument_list|(
name|toUri
argument_list|)
expr_stmt|;
block|}
DECL|method|getFromUri ()
specifier|public
name|String
name|getFromUri
parameter_list|()
block|{
return|return
name|fromUri
return|;
block|}
DECL|method|setFromUri (String fromUri)
specifier|public
name|void
name|setFromUri
parameter_list|(
name|String
name|fromUri
parameter_list|)
block|{
name|this
operator|.
name|fromUri
operator|=
name|fromUri
expr_stmt|;
block|}
DECL|method|getToUri ()
specifier|public
name|String
name|getToUri
parameter_list|()
block|{
return|return
name|toUri
return|;
block|}
DECL|method|setToUri (String toUri)
specifier|public
name|void
name|setToUri
parameter_list|(
name|String
name|toUri
parameter_list|)
block|{
name|this
operator|.
name|toUri
operator|=
name|toUri
expr_stmt|;
block|}
block|}
end_class

end_unit

