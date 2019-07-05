begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.pulsar.utils
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|pulsar
operator|.
name|utils
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_class
DECL|class|PulsarPath
specifier|public
class|class
name|PulsarPath
block|{
DECL|field|PATTERN
specifier|private
specifier|static
specifier|final
name|Pattern
name|PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^(persistent|non-persistent):?/?/(.+)/(.+)/(.+)$"
argument_list|)
decl_stmt|;
DECL|field|persistence
specifier|private
name|String
name|persistence
decl_stmt|;
DECL|field|tenant
specifier|private
name|String
name|tenant
decl_stmt|;
DECL|field|namespace
specifier|private
name|String
name|namespace
decl_stmt|;
DECL|field|topic
specifier|private
name|String
name|topic
decl_stmt|;
DECL|field|autoConfigurable
specifier|private
name|boolean
name|autoConfigurable
decl_stmt|;
DECL|method|PulsarPath (String path)
specifier|public
name|PulsarPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|Matcher
name|matcher
init|=
name|PATTERN
operator|.
name|matcher
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|autoConfigurable
operator|=
name|matcher
operator|.
name|matches
argument_list|()
expr_stmt|;
if|if
condition|(
name|autoConfigurable
condition|)
block|{
name|persistence
operator|=
name|matcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|tenant
operator|=
name|matcher
operator|.
name|group
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|namespace
operator|=
name|matcher
operator|.
name|group
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|topic
operator|=
name|matcher
operator|.
name|group
argument_list|(
literal|4
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getPersistence ()
specifier|public
name|String
name|getPersistence
parameter_list|()
block|{
return|return
name|persistence
return|;
block|}
DECL|method|getTenant ()
specifier|public
name|String
name|getTenant
parameter_list|()
block|{
return|return
name|tenant
return|;
block|}
DECL|method|getNamespace ()
specifier|public
name|String
name|getNamespace
parameter_list|()
block|{
return|return
name|namespace
return|;
block|}
DECL|method|getTopic ()
specifier|public
name|String
name|getTopic
parameter_list|()
block|{
return|return
name|topic
return|;
block|}
DECL|method|isAutoConfigurable ()
specifier|public
name|boolean
name|isAutoConfigurable
parameter_list|()
block|{
return|return
name|autoConfigurable
return|;
block|}
block|}
end_class

end_unit

