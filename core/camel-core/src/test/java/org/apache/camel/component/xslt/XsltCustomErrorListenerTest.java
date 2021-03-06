begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xslt
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|xslt
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|ErrorListener
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerException
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
name|TestSupport
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
name|impl
operator|.
name|DefaultCamelContext
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

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|XsltCustomErrorListenerTest
specifier|public
class|class
name|XsltCustomErrorListenerTest
extends|extends
name|TestSupport
block|{
DECL|field|listener
specifier|private
name|MyErrorListener
name|listener
init|=
operator|new
name|MyErrorListener
argument_list|()
decl_stmt|;
DECL|class|MyErrorListener
specifier|private
class|class
name|MyErrorListener
implements|implements
name|ErrorListener
block|{
DECL|field|warning
specifier|private
name|boolean
name|warning
decl_stmt|;
DECL|field|error
specifier|private
name|boolean
name|error
decl_stmt|;
DECL|field|fatalError
specifier|private
name|boolean
name|fatalError
decl_stmt|;
annotation|@
name|Override
DECL|method|warning (TransformerException exception)
specifier|public
name|void
name|warning
parameter_list|(
name|TransformerException
name|exception
parameter_list|)
throws|throws
name|TransformerException
block|{
name|warning
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|error (TransformerException exception)
specifier|public
name|void
name|error
parameter_list|(
name|TransformerException
name|exception
parameter_list|)
throws|throws
name|TransformerException
block|{
name|error
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|fatalError (TransformerException exception)
specifier|public
name|void
name|fatalError
parameter_list|(
name|TransformerException
name|exception
parameter_list|)
throws|throws
name|TransformerException
block|{
name|fatalError
operator|=
literal|true
expr_stmt|;
block|}
DECL|method|isWarning ()
specifier|public
name|boolean
name|isWarning
parameter_list|()
block|{
return|return
name|warning
return|;
block|}
DECL|method|isError ()
specifier|public
name|boolean
name|isError
parameter_list|()
block|{
return|return
name|error
return|;
block|}
DECL|method|isFatalError ()
specifier|public
name|boolean
name|isFatalError
parameter_list|()
block|{
return|return
name|fatalError
return|;
block|}
block|}
annotation|@
name|Test
DECL|method|testErrorListener ()
specifier|public
name|void
name|testErrorListener
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|RouteBuilder
name|builder
init|=
name|createRouteBuilder
argument_list|()
decl_stmt|;
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|bind
argument_list|(
literal|"myListener"
argument_list|,
name|listener
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
name|builder
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception due XSLT file not found"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// expected
block|}
name|assertFalse
argument_list|(
name|listener
operator|.
name|isWarning
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"My error listener should been invoked"
argument_list|,
name|listener
operator|.
name|isError
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"My error listener should been invoked"
argument_list|,
name|listener
operator|.
name|isFatalError
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"seda:a"
argument_list|)
operator|.
name|to
argument_list|(
literal|"xslt:org/apache/camel/builder/xml/example-with-errors.xsl?errorListener=#myListener"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

