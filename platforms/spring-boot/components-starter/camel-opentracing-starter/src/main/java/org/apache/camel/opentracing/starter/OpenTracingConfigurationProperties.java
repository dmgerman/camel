begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.opentracing.starter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|opentracing
operator|.
name|starter
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

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

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.opentracing"
argument_list|)
DECL|class|OpenTracingConfigurationProperties
specifier|public
class|class
name|OpenTracingConfigurationProperties
block|{
comment|/**      * Sets exclude pattern(s) that will disable tracing for Camel messages that      * matches the pattern.      */
DECL|field|excludePatterns
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|excludePatterns
decl_stmt|;
comment|/**      * Activate or deactivate dash encoding in headers (required by JMS) for      * messaging      */
DECL|field|encoding
specifier|private
name|Boolean
name|encoding
decl_stmt|;
DECL|method|getExcludePatterns ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getExcludePatterns
parameter_list|()
block|{
return|return
name|excludePatterns
return|;
block|}
DECL|method|setExcludePatterns (Set<String> excludePatterns)
specifier|public
name|void
name|setExcludePatterns
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|excludePatterns
parameter_list|)
block|{
name|this
operator|.
name|excludePatterns
operator|=
name|excludePatterns
expr_stmt|;
block|}
DECL|method|getEncoding ()
specifier|public
name|Boolean
name|getEncoding
parameter_list|()
block|{
return|return
name|encoding
return|;
block|}
DECL|method|setEncoding (Boolean encoding)
specifier|public
name|void
name|setEncoding
parameter_list|(
name|Boolean
name|encoding
parameter_list|)
block|{
name|this
operator|.
name|encoding
operator|=
name|encoding
expr_stmt|;
block|}
block|}
end_class

end_unit

