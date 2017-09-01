begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.foo
package|package
name|org
operator|.
name|foo
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
name|component
operator|.
name|connector
operator|.
name|ConnectorCustomizer
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
name|foo
operator|.
name|search
operator|.
name|TwitterSearchComponent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
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
name|EnableConfigurationProperties
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Configuration
import|;
end_import

begin_class
annotation|@
name|Configuration
annotation|@
name|EnableConfigurationProperties
argument_list|(
name|TwitterSearchCustomizerProperties
operator|.
name|class
argument_list|)
DECL|class|TwitterSearchCustomizer
specifier|public
class|class
name|TwitterSearchCustomizer
implements|implements
name|ConnectorCustomizer
argument_list|<
name|TwitterSearchComponent
argument_list|>
block|{
annotation|@
name|Autowired
DECL|field|configuration
specifier|private
name|TwitterSearchCustomizerProperties
name|configuration
decl_stmt|;
annotation|@
name|Override
DECL|method|customize (TwitterSearchComponent component)
specifier|public
name|void
name|customize
parameter_list|(
name|TwitterSearchComponent
name|component
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getKeywords
argument_list|()
argument_list|)
condition|)
block|{
name|component
operator|.
name|addOption
argument_list|(
literal|"keywords"
argument_list|,
name|configuration
operator|.
name|getKeywords
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|String
name|keywords
init|=
operator|(
name|String
operator|)
name|component
operator|.
name|getOptions
argument_list|()
operator|.
name|get
argument_list|(
literal|"keywords"
argument_list|)
decl_stmt|;
name|String
name|prefix
init|=
operator|(
name|String
operator|)
name|component
operator|.
name|getOptions
argument_list|()
operator|.
name|get
argument_list|(
literal|"prefix"
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|keywords
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|prefix
argument_list|)
condition|)
block|{
name|component
operator|.
name|addOption
argument_list|(
literal|"keywords"
argument_list|,
name|prefix
operator|+
name|keywords
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

