begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.web.groovy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|web
operator|.
name|groovy
package|;
end_package

begin_comment
comment|/**  *   */
end_comment

begin_class
DECL|class|InterceptFromDSLTest
specifier|public
class|class
name|InterceptFromDSLTest
extends|extends
name|GroovyRendererTestSupport
block|{
DECL|method|testInterceptFromChoice ()
specifier|public
name|void
name|testInterceptFromChoice
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|dsl
init|=
literal|"interceptFrom().choice().when(header(\"foo\").isEqualTo(\"bar\")).to(\"mock:b\").stop().end();from(\"direct:start\").to(\"mock:a\")"
decl_stmt|;
name|String
name|expectedDSL
init|=
name|dsl
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedDSL
argument_list|,
name|render
argument_list|(
name|dsl
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testInterceptFromPredicateWithStop ()
specifier|public
name|void
name|testInterceptFromPredicateWithStop
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|dsl
init|=
literal|"interceptFrom().when(header(\"usertype\").isEqualTo(\"test\")).stop();from(\"direct:start\").to(\"mock:result\")"
decl_stmt|;
name|String
name|expectedDSL
init|=
literal|"interceptFrom().choice().when(header(\"usertype\").isEqualTo(\"test\")).stop().end();from(\"direct:start\").to(\"mock:result\")"
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedDSL
argument_list|,
name|render
argument_list|(
name|dsl
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testInterceptFromToLog ()
specifier|public
name|void
name|testInterceptFromToLog
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|dsl
init|=
literal|"interceptFrom().to(\"log:received\");from(\"direct:start\").to(\"mock:result\")"
decl_stmt|;
name|String
name|expectedDSL
init|=
name|dsl
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedDSL
argument_list|,
name|render
argument_list|(
name|dsl
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testInterceptFromUriRegex ()
specifier|public
name|void
name|testInterceptFromUriRegex
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|dsl
init|=
literal|"interceptFrom(\"seda:(bar|foo)\").to(\"mock:intercept\");"
operator|+
literal|"from(\"direct:start\").to(\"mock:result\");from(\"seda:bar\").to(\"mock:result\");"
operator|+
literal|"from(\"seda:foo\").to(\"mock:result\");from(\"seda:cheese\").to(\"mock:result\")"
decl_stmt|;
name|String
name|expectedDSL
init|=
literal|"from(\"direct:start\").to(\"mock:result\");"
operator|+
literal|"interceptFrom(\"seda:(bar|foo)\").to(\"mock:intercept\");from(\"seda:bar\").to(\"mock:result\");"
operator|+
literal|"interceptFrom(\"seda:(bar|foo)\").to(\"mock:intercept\");from(\"seda:foo\").to(\"mock:result\");"
operator|+
literal|"from(\"seda:cheese\").to(\"mock:result\")"
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedDSL
argument_list|,
name|renderRoutes
argument_list|(
name|dsl
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testInterceptFromUriSimpleLog ()
specifier|public
name|void
name|testInterceptFromUriSimpleLog
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|dsl
init|=
literal|"interceptFrom(\"seda:bar\").to(\"mock:bar\");"
operator|+
literal|"from(\"direct:start\").to(\"mock:first\").to(\"seda:bar\");"
operator|+
literal|"from(\"seda:bar\").to(\"mock:result\");from(\"seda:foo\").to(\"mock:result\")"
decl_stmt|;
name|String
name|expectedDSL
init|=
literal|"from(\"direct:start\").to(\"mock:first\").to(\"seda:bar\");"
operator|+
literal|"interceptFrom(\"seda:bar\").to(\"mock:bar\");from(\"seda:bar\").to(\"mock:result\");"
operator|+
literal|"from(\"seda:foo\").to(\"mock:result\")"
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedDSL
argument_list|,
name|renderRoutes
argument_list|(
name|dsl
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testInterceptFromUriWildcard ()
specifier|public
name|void
name|testInterceptFromUriWildcard
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|dsl
init|=
literal|"interceptFrom(\"seda*\").to(\"mock:intercept\");"
operator|+
literal|"from(\"direct:start\").to(\"mock:result\");from(\"seda:bar\").to(\"mock:result\");"
operator|+
literal|"from(\"seda:foo\").to(\"mock:result\")"
decl_stmt|;
name|String
name|expectedDSL
init|=
literal|"from(\"direct:start\").to(\"mock:result\");"
operator|+
literal|"interceptFrom(\"seda*\").to(\"mock:intercept\");from(\"seda:bar\").to(\"mock:result\");"
operator|+
literal|"interceptFrom(\"seda*\").to(\"mock:intercept\");from(\"seda:foo\").to(\"mock:result\")"
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedDSL
argument_list|,
name|renderRoutes
argument_list|(
name|dsl
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testInterceptFromWithPredicate ()
specifier|public
name|void
name|testInterceptFromWithPredicate
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|dsl
init|=
literal|"interceptFrom().when(header(\"foo\").isEqualTo(\"bar\")).to(\"mock:b\").stop();from(\"direct:start\").to(\"mock:a\")"
decl_stmt|;
name|String
name|expectedDSL
init|=
literal|"interceptFrom().choice().when(header(\"foo\").isEqualTo(\"bar\")).to(\"mock:b\").stop().end();from(\"direct:start\").to(\"mock:a\")"
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedDSL
argument_list|,
name|render
argument_list|(
name|dsl
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

