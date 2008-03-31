begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.uface
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|uface
package|;
end_package

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
name|Exchange
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
name|component
operator|.
name|list
operator|.
name|ListEndpoint
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

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|core
operator|.
name|databinding
operator|.
name|observable
operator|.
name|Realm
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|core
operator|.
name|databinding
operator|.
name|observable
operator|.
name|list
operator|.
name|WritableList
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|UFaceEndpoint
specifier|public
class|class
name|UFaceEndpoint
extends|extends
name|ListEndpoint
block|{
DECL|method|UFaceEndpoint (String uri, UFaceComponent component)
specifier|public
name|UFaceEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|UFaceComponent
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createExchangeList ()
specifier|protected
name|List
argument_list|<
name|Exchange
argument_list|>
name|createExchangeList
parameter_list|()
block|{
name|Realm
name|realm
init|=
name|Realm
operator|.
name|getDefault
argument_list|()
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|realm
argument_list|,
literal|"DataBinding Realm"
argument_list|)
expr_stmt|;
return|return
operator|new
name|WritableList
argument_list|(
name|realm
argument_list|)
return|;
block|}
block|}
end_class

end_unit

