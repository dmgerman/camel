begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.core.osgi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|core
operator|.
name|osgi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|CamelContext
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
name|Expression
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
name|Predicate
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
name|impl
operator|.
name|DefaultCamelContext
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
name|impl
operator|.
name|SimpleRegistry
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
name|Language
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|OsgiLanguageResolverTest
specifier|public
class|class
name|OsgiLanguageResolverTest
extends|extends
name|CamelOsgiTestSupport
block|{
annotation|@
name|Test
DECL|method|testOsgiResolverFindLanguageTest ()
specifier|public
name|void
name|testOsgiResolverFindLanguageTest
parameter_list|()
throws|throws
name|IOException
block|{
name|CamelContext
name|camelContext
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|OsgiLanguageResolver
name|resolver
init|=
operator|new
name|OsgiLanguageResolver
argument_list|(
name|getBundleContext
argument_list|()
argument_list|)
decl_stmt|;
name|Language
name|language
init|=
name|resolver
operator|.
name|resolveLanguage
argument_list|(
literal|"simple"
argument_list|,
name|camelContext
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"We should find simple language"
argument_list|,
name|language
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testOsgiResolverFindLanguageFallbackTest ()
specifier|public
name|void
name|testOsgiResolverFindLanguageFallbackTest
parameter_list|()
throws|throws
name|IOException
block|{
name|SimpleRegistry
name|registry
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|put
argument_list|(
literal|"fuffy-language"
argument_list|,
operator|new
name|SampleLanguage
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|CamelContext
name|camelContext
init|=
operator|new
name|DefaultCamelContext
argument_list|(
name|registry
argument_list|)
decl_stmt|;
name|OsgiLanguageResolver
name|resolver
init|=
operator|new
name|OsgiLanguageResolver
argument_list|(
name|getBundleContext
argument_list|()
argument_list|)
decl_stmt|;
name|Language
name|language
init|=
name|resolver
operator|.
name|resolveLanguage
argument_list|(
literal|"fuffy"
argument_list|,
name|camelContext
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"We should find fuffy language"
argument_list|,
name|language
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"We should find the fallback language"
argument_list|,
operator|(
operator|(
name|SampleLanguage
operator|)
name|language
operator|)
operator|.
name|isFallback
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testOsgiResolverFindLanguageDoubleFallbackTest ()
specifier|public
name|void
name|testOsgiResolverFindLanguageDoubleFallbackTest
parameter_list|()
throws|throws
name|IOException
block|{
name|SimpleRegistry
name|registry
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|put
argument_list|(
literal|"fuffy"
argument_list|,
operator|new
name|SampleLanguage
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|registry
operator|.
name|put
argument_list|(
literal|"fuffy-language"
argument_list|,
operator|new
name|SampleLanguage
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|CamelContext
name|camelContext
init|=
operator|new
name|DefaultCamelContext
argument_list|(
name|registry
argument_list|)
decl_stmt|;
name|OsgiLanguageResolver
name|resolver
init|=
operator|new
name|OsgiLanguageResolver
argument_list|(
name|getBundleContext
argument_list|()
argument_list|)
decl_stmt|;
name|Language
name|language
init|=
name|resolver
operator|.
name|resolveLanguage
argument_list|(
literal|"fuffy"
argument_list|,
name|camelContext
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"We should find fuffy language"
argument_list|,
name|language
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"We should NOT find the fallback language"
argument_list|,
operator|(
operator|(
name|SampleLanguage
operator|)
name|language
operator|)
operator|.
name|isFallback
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|SampleLanguage
specifier|private
specifier|static
class|class
name|SampleLanguage
implements|implements
name|Language
block|{
DECL|field|fallback
specifier|private
name|boolean
name|fallback
decl_stmt|;
DECL|method|SampleLanguage (boolean fallback)
specifier|public
name|SampleLanguage
parameter_list|(
name|boolean
name|fallback
parameter_list|)
block|{
name|this
operator|.
name|fallback
operator|=
name|fallback
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createPredicate (String expression)
specifier|public
name|Predicate
name|createPredicate
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Should not be called"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|createExpression (String expression)
specifier|public
name|Expression
name|createExpression
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Should not be called"
argument_list|)
throw|;
block|}
DECL|method|isFallback ()
specifier|public
name|boolean
name|isFallback
parameter_list|()
block|{
return|return
name|fallback
return|;
block|}
DECL|method|setFallback (boolean fallback)
specifier|public
name|void
name|setFallback
parameter_list|(
name|boolean
name|fallback
parameter_list|)
block|{
name|this
operator|.
name|fallback
operator|=
name|fallback
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

