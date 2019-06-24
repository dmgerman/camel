begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.any23
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|any23
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
name|Message
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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
DECL|class|Any23DataFormatBasicTest
specifier|public
class|class
name|Any23DataFormatBasicTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testUnMarshalToStringOfXml ()
specifier|public
name|void
name|testUnMarshalToStringOfXml
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|resultEndpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:result"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
comment|//resultEndpoint.expectedMessageCount(2);
comment|//  String badHtml = TidyMarkupTestSupport.loadFileAsString(new File(
comment|//          "src/test/resources/org/apache/camel/dataformat/any23/testfile1.html"));
comment|//   String evilHtml = TidyMarkupTestSupport.loadFileAsString(new File(
comment|//          "src/test/resources/org/apache/camel/dataformat/any23/testfile2-evilHtml.html"));
name|String
name|contenhtml
init|=
literal|"<div id='hcard-JOSE-LUIS-SEGARRA-FLORES' class='vcard'> "
operator|+
literal|"<a class='url fn n' href='https://www.youtube.com/watch?v=kg1BljLu9YY'><span class='given-name'>JOSE</span> "
operator|+
literal|"<span class='additional-name'>LUIS</span> "
operator|+
literal|"<span class='family-name'>SEGARRA FLORES</span> "
operator|+
literal|"</a> "
operator|+
literal|"<div class='org'>TransExpress</div> "
operator|+
literal|"<a class='email' href='mailto:joesega7@gmail.com'>joesega7@gmail.com</a> "
operator|+
literal|"<div class='adr'> "
operator|+
literal|"<div class='street-address'>7801 NW 37th Street Doral,  FL 33195-6503</div> "
operator|+
literal|"<span class='locality'>Doral</span> "
operator|+
literal|",  "
operator|+
literal|"<span class='region'>Florida</span> "
operator|+
literal|",  "
operator|+
literal|"<span class='postal-code'>33195-6503</span> "
operator|+
literal|" "
operator|+
literal|"<span class='country-name'>Estados Unidos</span> "
operator|+
literal|" "
operator|+
literal|"</div> "
operator|+
literal|"<div class='tel'>3055920839</div> "
operator|+
literal|"<p style='font-size:smaller;'>This<a href='http://microformats.org/wiki/hcard'>hCard</a> created with the<a href='http://microformats.org/code/hcard/creator'>hCard creator</a>.</p> "
operator|+
literal|"</div>"
decl_stmt|;
specifier|final
name|String
name|content
init|=
literal|"<span class='vcard'> "
operator|+
literal|"<span class='fn'>L'Amourita Pizza</span> "
operator|+
literal|"   Located at "
operator|+
literal|"<span class='adr'> "
operator|+
literal|"<span class='street-address'>123 Main St</span>, "
operator|+
literal|"<span class='locality'>Albequerque</span>, "
operator|+
literal|"<span class='region'>NM</span>. "
operator|+
literal|"</span> "
operator|+
literal|"<a href='http://pizza.example.com' class='url'>http://pizza.example.com</a> "
operator|+
literal|"</span>   "
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|contenhtml
argument_list|)
expr_stmt|;
comment|//  template.sendBody("direct:start", evilHtml);
comment|//resultEndpoint.assertIsSatisfied();
name|List
argument_list|<
name|Exchange
argument_list|>
name|list
init|=
name|resultEndpoint
operator|.
name|getReceivedExchanges
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|print
argument_list|(
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Exchange
name|exchange
range|:
name|list
control|)
block|{
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
comment|//  Node tidyMarkup = in.getBody(Node.class);
name|System
operator|.
name|out
operator|.
name|print
argument_list|(
name|in
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Received "
operator|+
name|in
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
comment|// assertNotNull("Should be able to convert received body to a string", tidyMarkup);
block|}
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
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
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|marshal
argument_list|()
operator|.
name|any23
argument_list|(
literal|"http://pizza.example.com"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|//from("direct:start").unmarshal().any23().to("mock:result");
comment|//  from("direct:start").marshal().tidyMarkup();
comment|//  from("direct:start").unmarshal().tidyMarkup().to("mock:result");
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

