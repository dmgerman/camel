begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.component.file.remote
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
operator|.
name|remote
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
name|ContextTestSupport
import|;
end_import

begin_comment
comment|/**  * Unit test used for FTP wiki documentation  */
end_comment

begin_class
DECL|class|FromFtpToBinarySampleTest
specifier|public
class|class
name|FromFtpToBinarySampleTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testDummy ()
specifier|public
name|void
name|testDummy
parameter_list|()
throws|throws
name|Exception
block|{
comment|// this is a noop test
block|}
comment|// START SNIPPET: e1
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
comment|// we use a delay of 60 minutes (eg. once pr. hour we poll the FTP server
name|long
name|delay
init|=
literal|60
operator|*
literal|60
operator|*
literal|1000L
decl_stmt|;
comment|// from the given FTP server we poll (= download) all the files
comment|// from the public/reports folder as BINARY types and store this as files
comment|// in a local directory. Camle will use the filenames from the FTPServer
comment|// notice that the FTPConsumer properties must be prefixed with "consumer." in the URL
comment|// the delay parameter is from the FileConsumer component so we should use consumer.delay as
comment|// the URI parameter name. The FTP Component is an extension of the File Component.
name|from
argument_list|(
literal|"ftp://scott@localhost/public/reports?password=tiger&binary=true&consumer.delay="
operator|+
name|delay
argument_list|)
operator|.
name|to
argument_list|(
literal|"file://target/test-reports"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
comment|// END SNIPPET: e1
block|}
end_class

end_unit

