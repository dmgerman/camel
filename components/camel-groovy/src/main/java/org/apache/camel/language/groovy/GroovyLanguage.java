begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.groovy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|groovy
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|groovy
operator|.
name|lang
operator|.
name|Script
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
name|Service
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
name|spi
operator|.
name|annotations
operator|.
name|Language
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
name|support
operator|.
name|LRUCacheFactory
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
name|support
operator|.
name|LanguageSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|groovy
operator|.
name|runtime
operator|.
name|InvokerHelper
import|;
end_import

begin_class
annotation|@
name|Language
argument_list|(
literal|"groovy"
argument_list|)
DECL|class|GroovyLanguage
specifier|public
class|class
name|GroovyLanguage
extends|extends
name|LanguageSupport
block|{
comment|// Cache used to stores the compiled scripts (aka their classes)
DECL|field|scriptCache
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|GroovyClassService
argument_list|>
name|scriptCache
init|=
name|LRUCacheFactory
operator|.
name|newLRUSoftCache
argument_list|(
literal|16
argument_list|,
literal|1000
argument_list|,
literal|true
argument_list|)
decl_stmt|;
DECL|class|GroovyClassService
specifier|private
specifier|static
specifier|final
class|class
name|GroovyClassService
implements|implements
name|Service
block|{
DECL|field|script
specifier|private
specifier|final
name|Class
argument_list|<
name|Script
argument_list|>
name|script
decl_stmt|;
DECL|method|GroovyClassService (Class<Script> script)
specifier|private
name|GroovyClassService
parameter_list|(
name|Class
argument_list|<
name|Script
argument_list|>
name|script
parameter_list|)
block|{
name|this
operator|.
name|script
operator|=
name|script
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
block|{         }
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
name|InvokerHelper
operator|.
name|removeClass
argument_list|(
name|script
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|groovy (String expression)
specifier|public
specifier|static
name|GroovyExpression
name|groovy
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
return|return
operator|new
name|GroovyLanguage
argument_list|()
operator|.
name|createExpression
argument_list|(
name|expression
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createPredicate (String expression)
specifier|public
name|GroovyExpression
name|createPredicate
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
return|return
name|createExpression
argument_list|(
name|expression
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createExpression (String expression)
specifier|public
name|GroovyExpression
name|createExpression
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
name|expression
operator|=
name|loadResource
argument_list|(
name|expression
argument_list|)
expr_stmt|;
return|return
operator|new
name|GroovyExpression
argument_list|(
name|expression
argument_list|)
return|;
block|}
DECL|method|getScriptFromCache (String script)
name|Class
argument_list|<
name|Script
argument_list|>
name|getScriptFromCache
parameter_list|(
name|String
name|script
parameter_list|)
block|{
specifier|final
name|GroovyClassService
name|cached
init|=
name|scriptCache
operator|.
name|get
argument_list|(
name|script
argument_list|)
decl_stmt|;
if|if
condition|(
name|cached
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|cached
operator|.
name|script
return|;
block|}
DECL|method|addScriptToCache (String script, Class<Script> scriptClass)
name|void
name|addScriptToCache
parameter_list|(
name|String
name|script
parameter_list|,
name|Class
argument_list|<
name|Script
argument_list|>
name|scriptClass
parameter_list|)
block|{
name|scriptCache
operator|.
name|put
argument_list|(
name|script
argument_list|,
operator|new
name|GroovyClassService
argument_list|(
name|scriptClass
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

